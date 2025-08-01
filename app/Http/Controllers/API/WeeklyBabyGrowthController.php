<?php

namespace App\Http\Controllers\API;

use App\Http\Controllers\Controller;
use App\Models\WeeklyBabyGrowth;
use Illuminate\Http\Request;

class WeeklyBabyGrowthController extends BaseController
{
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        $data = WeeklyBabyGrowth::all();
        return $this->sendResponse($data);
    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request)
    {
        //
    }

    /**
     * Display the specified resource.
     */
    public function show(WeeklyBabyGrowth $weeklyBabyGrowth)
    {
        $data = WeeklyBabyGrowth::with('user.doctor')->find($weeklyBabyGrowth->id);
        return $this->sendResponse($data);
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, WeeklyBabyGrowth $weeklyBabyGrowth)
    {
        //
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy(WeeklyBabyGrowth $weeklyBabyGrowth)
    {
        //
    }
}
