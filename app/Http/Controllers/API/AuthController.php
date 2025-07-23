<?php

namespace App\Http\Controllers\API;

use App\Http\Controllers\Controller;
use App\Models\NormalPeople;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Validator;

class AuthController extends BaseController
{
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

    public function changePassword(Request $request) {
        $validator = Validator::make($request->all(), [
            'old_password' => 'required|min:8',
            'new_password' => 'required|min:8',
            'confirm_password' => 'required|same:password',
        ]);

        if($validator->fails()){
            return $this->validationError($validator);
        }

        $auth = User::find(Auth::id());

        if(!Hash::check($request->new_password, $auth->password)){
            return $this->sendError('Old password did not match.', 400);
        }

        $auth->password = Hash::make($request->new_password);

        return $this->sendResponse(null, 'accepted', 202);
    }
}
