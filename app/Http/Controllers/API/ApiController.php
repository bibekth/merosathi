<?php

namespace App\Http\Controllers\API;

use Illuminate\Http\Request;
use Throwable;

class ApiController extends BaseController
{
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
}
