<?php

namespace App\Http\Controllers\API;

use App\Http\Controllers\Controller;
use App\Models\BodyChange;
use Illuminate\Http\Request;

class BodyChangeController extends BaseController
{
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        $data = BodyChange::all();
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
    public function show(BodyChange $bodyChange)
    {
        $data = BodyChange::with('user.doctor')->find($bodyChange->id);
        return $this->sendResponse($data);
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, BodyChange $bodyChange)
    {
        //
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy(BodyChange $bodyChange)
    {
        //
    }
}
