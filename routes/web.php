<?php

use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Route;

Route::get('/', function () {
    // return view('welcome');
    return redirect()->route('home');
});

Auth::routes();

Route::get('/home', [App\Http\Controllers\HomeController::class, 'index'])->name('home')->middleware('auth:sanctum');

Route::group(['namespace' => 'App\Http\Controllers', 'middleware' => 'auth:sanctum'], function(){
    Route::resource('users', 'UserController')->middleware('role:admin');
    Route::resource('doctors', 'DoctorController')->middleware('role:admin');
    Route::resource('articles', 'ArticleController')->middleware('role:admin,doctor');
    Route::resource('article-sections', 'ArticleSectionController')->middleware('role:admin,doctor');
    Route::resource('babies', 'WeeklyBabyGrowthController')->middleware('role:admin,doctor');
    Route::resource('bodies', 'BodyChangeController')->middleware('role:admin,doctor');
});
