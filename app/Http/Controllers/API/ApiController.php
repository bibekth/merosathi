<?php

namespace App\Http\Controllers\API;

use App\Models\NormalPeople;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Hash;
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

    /**
     * ---------------------------------------------------------------------------------
     *
     * Authentication Section Begins
     *
     * ---------------------------------------------------------------------------------
     */

    /**
     * Login function
     */
    public function login(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'email' => 'required|email|exists:users,email',
            'password' => 'required|min:8'
        ]);

        if ($validator->fails()) {
            return $this->validationError($validator);
        }

        $data = $request->only(['email', 'password']);

        $user = User::where('email', $data['email'])->first();

        if (!Hash::check($data['password'], $user->password)) {
            return $this->sendError('Credentials did not match.', null, 401);
        }

        Auth::login($user);
        $token = $user->createToken($data['email']);
        $tokenPart = explode('|', $token->plainTextToken);

        return $this->sendResponse(['token' => $tokenPart[1]]);
    }

    /**
     * Register Function
     */
    public function register(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'name' => 'required',
            'username' => 'required|unique:normal_people,username',
            'contact' => 'required|unique:normal_people,contact',
            'email' => 'required|email|unique:normal_people,email',
            'dob' => 'required|date',
            'password' => 'required|min:8',
            'confirm_password' => 'required|same:password'
        ]);

        if ($validator->fails()) {
            return $this->validationError($validator);
        }

        $user = User::create(['name' => $request->name, 'email' => $request->email, 'password' => Hash::make($request->password)]);

        $data = $request->only(['name', 'username', 'contact', 'email', 'dob']);

        $data['user_id'] = $user->id;

        NormalPeople::create($data);

        return $this->sendResponse(null, 'created', 201);
    }
}
