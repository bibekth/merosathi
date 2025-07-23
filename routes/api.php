<?php

use App\Http\Controllers\API\ApiController;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

Route::get('/user', function (Request $request) {
    return $request->user();
})->middleware('auth:sanctum');


Route::group(['namespace' => 'App\Http\Controllers\API', 'as' => 'api.'], function () {
    Route::group(['controller' => 'ApiController'], function () {
        Route::post('github-webhooks', 'githubWebhook');
    });

    Route::group(['controller' => 'AuthController'], function () {
        Route::post('login', 'login')->name('login');
        Route::post('register', 'register')->name('register');
        Route::post('change-password', 'changePassword')->name('change.password');
    });
});
