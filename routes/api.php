<?php

use App\Http\Controllers\API\ApiController;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

Route::get('/user', function (Request $request) {
    return $request->user();
})->middleware('auth:sanctum');


Route::group(['namespace' => 'App\Http\Controllers\API', 'as' => 'api.'], function () {
    Route::group(['controller' => 'AuthController'], function () {
        Route::post('login', 'login')->name('login');
        Route::post('register', 'register')->name('register');
        Route::post('change-password', 'changePassword')->name('change.password');
    });
    Route::middleware('auth:sanctum')->group(function () {
        Route::group(['controller' => 'ApiController'], function () {
            Route::post('github-webhooks', 'githubWebhook')->withoutMiddleware('api');
            Route::post('calculate', 'calculateDay')->name('calculate.days');
            Route::get('main', 'main')->name('main');
        });
        Route::apiResource('articles', 'ArticleController')->only(['index', 'show']);
        Route::apiResource('body-changes', 'BodyChangeController')->only(['index', 'show']);
        Route::apiResource('weekly-baby-growth', 'WeeklyBabyGrowthController')->only(['index', 'show']);
    });
});
