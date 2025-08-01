<?php

namespace App\Http\Controllers\API;

use App\Models\BodyChange;
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

    public function calculateDay(Request $request) {
        $validator = Validator::make($request->all(), [
            'lmp' => 'date',
            'weeks' => 'numeric'
        ]);

        if(!$request->filled('lmp') && !$request->filled('weeks')){
            return $this->sendError('At least lmp or weeks field is required.', null, 422);
        }

        if($validator->fails()) {
            return $this->validationError($validator);
        }

        $auth = User::find(Auth::id());
        $person = $auth->person;
        if($request->filled('lmp')){
            $person->lmp = $request->lmp;
            $endDate = Carbon::parse($request->lmp)->addWeeks(40);
        }elseif($request->filled('weeks')){
            $endDate = Carbon::parse(today())->subWeeks($request->weeks)->addWeeks(40);
        }

        $person->expected_date = $endDate->format('Y-m-d');
        $person->save();

        return $this->sendResponse(['deliver date' => $endDate->format('Y-m-d')]);
    }

    public function main(Request $request)
    {
         $validator = Validator::make($request->all(), [
            'date' => 'date',
        ]);

        if($validator->fails()) {
            return $this->validationError($validator);
        }

        $auth = User::find(Auth::id());

        $date = Carbon::parse($request->date) ?? today();

        $expectedDate = Carbon::parse($auth->person->expected_date);

        $weeks = (40 - $expectedDate->diffInWeeks($date, true));
        if($weeks - floor($weeks) > 0){
            $weeks = (int) ($weeks + 1);
        }else{
            $weeks = (int) $weeks;
        }
        $data['baby_growth'] = WeeklyBabyGrowth::where('weeks', $weeks)->first();
        $data['body_change'] = BodyChange::where('weeks', $weeks)->first();

        return $this->sendResponse($data);
    }
}
