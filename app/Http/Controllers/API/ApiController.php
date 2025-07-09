<?php

namespace App\Http\Controllers\API;

use Illuminate\Http\Request;
use Throwable;

class ApiController extends BaseController
{
    public function gitlabWebhook(Request $request)
    {
        try {
            $secret = 'monkey@21';
            $gitlabToken = $request->header('X-Gitlab-Token');

            if ($gitlabToken !== $secret) {
                return $this->sendError('Invalid Token', null, 403);
            }

            $data = $request->all();

            if (isset($data['ref']) && $data['ref'] === 'refs/heads/development') {
                $output = [];
                $returnCode = 0;

                exec(`eval "$(ssh-agent -s)" && ssh-add ~/.ssh/merogrocery && cd ~/public_html/merogrocery && git pull origin development 2>&1`, $output, $returnCode);

                file_put_contents("gitlab_webhook.log", implode("\n", $output) . "\n", FILE_APPEND);
            }

            return $this->sendResponse(null, 'success', 200);
        } catch (Throwable $e) {
            return $this->sendServerError($e->getMessage());
        }
    }
}
