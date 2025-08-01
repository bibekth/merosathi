<?php

namespace App\Http\Controllers\API;

use App\Http\Controllers\Controller;
use Illuminate\Http\Exceptions\HttpResponseException;

class BaseController extends Controller
{
    /**
     * success response method.
     *
     * @return \Illuminate\Http\Response
     */
    public function sendResponse($result = null, $message = 'success', $code = 200, $pagination = null)
    {
        $response = [
            'success' => true,
        ];
        if (!empty($message) || $message !== null) {
            $response['message'] = $message;
        }
        if (!empty($result) || $result !== null) {
            $response['data'] = $result;
        }
        if ($pagination !== null) {
            $response['pagination'] = $pagination;
        }
        return response()->json($response, $code);
    }

    /**
     * return error response.
     *
     * @return \Illuminate\Http\Response
     */
    public function sendError($error = '', $errorMessages = [], $code = 404)
    {
        $response = [
            'success' => false,
        ];
        if (!empty($error)) {
            $response['message'] = $error;
        }
        if (!empty($errorMessages) || $errorMessages !== null) {
            $response['data'] = $errorMessages;
        }
        return response()->json($response, $code);
    }

    /**
     * return error response.
     *
     * @return \Illuminate\Http\Response
     */
    public function sendServerError($error, $code = 500)
    {
        $response = [
            'success' => false,
            'message' => $error
        ];

        return response()->json($response, $code);
    }

    /**
     * Handle a failed validation attempt.
     */
    public function validationError($validator)
    {
        throw new HttpResponseException(response()->json([
            'success'   => false,
            'message'   => 'Validation errors',
            'data'      => $validator->errors()
        ], 422));
    }
}
