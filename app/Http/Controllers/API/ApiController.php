<?php

namespace App\Http\Controllers\API;

use App\Models\BodyChange;
use App\Models\Like;
use App\Models\Notification;
use App\Models\User;
use App\Models\WeeklyBabyGrowth;
use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Validator;
use Throwable;

class ApiController extends BaseController
{
    /**
     * Function responsible for auto git update on push event.
     */
    public function githubWebhook(Request $request)
    {
        try {
            $secret = "monkey@21";
            $payload = file_get_contents("php://input");
            // file_put_contents("webhook_request.log", $payload, FILE_APPEND);
            $signature = $_SERVER["HTTP_X_HUB_SIGNATURE_256"] ?? "";
            $hash = "sha256=" . hash_hmac("sha256", $payload, $secret);
            if (!hash_equals($hash, $signature)) {
                http_response_code(403);
                exit("Invalid Signature");
            }

            $data = json_decode($payload, true);
            if ($data["ref"] === "refs/heads/web") {
                exec("cd ~/public_html/merosathi && git pull origin web 2>&1", $output, $returnCode);
                file_put_contents("webhook.log", implode('\n', $output), FILE_APPEND);
            }
            return response()->json('success', 200);
        } catch (Throwable $e) {
            return response()->json($e->getMessage(), 500);
        }
    }

    public function calculateDay(Request $request)
    {
        try {
            $auth = User::find(Auth::id());
            $person = $auth->person;
            if ($request->filled('lmp')) {
                $person->lmp = $request->lmp;
                $endDate = Carbon::parse($request->lmp)->addWeeks(40);
            } elseif ($request->filled('weeks')) {
                $endDate = Carbon::parse(today())->subWeeks($request->weeks)->addWeeks(40);
            }

            $person->expected_date = $endDate->format('Y-m-d');
            $person->save();

            return response()->json(['deliver date' => $endDate->format('Y-m-d')], 200);
            // return $this->sendResponse(['deliver date' => $endDate->format('Y-m-d')]);
        } catch (Throwable $e) {
            return $this->sendError($e->getMessage(), null, 500);
        }
    }

    public function main(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'date' => 'date',
        ]);

        if ($validator->fails()) {
            return $this->validationError($validator);
        }

        $auth = User::find(Auth::id());

        $date = $request->date ? Carbon::parse($request->date) : today();

        $expectedDate = Carbon::parse($auth->person->expected_date);

        // Pregnancy start = due date - 40 weeks
        $pregnancyStartDate = $expectedDate->copy()->subWeeks(40);

        // Total days since pregnancy started
        $daysSinceStart = $pregnancyStartDate->diffInDays($date);

        // Calculate weeks and days
        $weeks = floor($daysSinceStart / 7); // +1 to make it "Week 1" not "Week 0"
        $dayOfWeek = ($daysSinceStart % 7) + 1;  // Day 1-7 inside the week

        // Baby growth & body change based on current week
        $data['baby_growth'] = WeeklyBabyGrowth::where('week', $weeks)->first();
        $data['body_change'] = BodyChange::where('week', $weeks)->first();

        // Time description: "Week X, Day Y"
        $weekLabel = $weeks === 1 ? 'week' : 'weeks';
        $dayLabel  = $dayOfWeek === 1 ? 'day' : 'days';

        $data['time'] = "{$weeks} {$weekLabel}, {$dayOfWeek} {$dayLabel}";

        $this->notification($auth);

        return $this->sendResponse($data);
    }

    private function notification($auth)
    {
        // Always calculate week based on today's date
        $today = today();
        $expectedDate = Carbon::parse($auth->person->expected_date);
        $pregnancyStartDate = $expectedDate->copy()->subWeeks(40);
        $daysSinceStart = $pregnancyStartDate->diffInDays($today);
        $currentWeek = floor($daysSinceStart / 7);

        $weeks = [12, 16, 20, 28, 32, 34, 36, 38];

        $titles = [
            'First Visit',
            'Second Visit',
            'Third Visit',
            'Fourth Visit',
            'Fifth Visit',
            'Sixth Visit',
            'Seventh Visit',
            'Eighth Visit',
        ];

        $descriptions = [
            'Initial check-up: Confirm pregnancy, review medical history, perform physical exam, and basic blood/urine tests.',
            'Routine check-up: Monitor blood pressure, weight, and baby’s growth. Screen for early complications.',
            'Detailed ultrasound (anomaly scan): Check baby’s development, growth, and screen for structural conditions.',
            'Glucose tolerance test & routine checks: Monitor for gestational diabetes, anemia, and assess baby’s growth.',
            'Growth monitoring: Check baby’s position, heartbeat, and mother’s health status.',
            'Follow-up growth & blood pressure monitoring: Ensure no signs of pre-eclampsia and assess baby’s movements.',
            'Weekly visits start: Monitor baby’s heart rate, position, and mother’s readiness for delivery.',
            'Final visit before due date: Ensure baby’s position is correct, check labor signs, and prepare for delivery.',
        ];

        // Only consider weeks up to today's current week
        $availableWeeks = array_filter($weeks, function ($w) use ($currentWeek) {
            return $w <= $currentWeek;
        });

        foreach ($availableWeeks as $index => $availableWeek) {
            $exists = Notification::where('user_id', $auth->id)
                ->where('week', $availableWeek)
                ->exists();

            if (! $exists) {
                Notification::create([
                    'user_id'     => $auth->id,
                    'week'        => $availableWeek,
                    'title'       => $titles[$index],
                    'description' => $descriptions[$index],
                    'image'       => null,
                ]);
            }
        }
    }

    public function liked(Request $request)
    {
        $likeableTypes = ['article_id', 'weekly_baby_growth_id', 'body_change_id'];

        foreach ($likeableTypes as $type) {
            if ($request->filled($type)) {
                if ($request->liked) {
                    Like::firstOrCreate([
                        'user_id' => Auth::id(),
                        $type     => $request->$type,
                    ]);
                } else {
                    Like::where('user_id', Auth::id())
                        ->where($type, $request->$type)
                        ->delete();
                }
            }
        }

        return response()->json(['message' => 'success'], 200);
    }
}
