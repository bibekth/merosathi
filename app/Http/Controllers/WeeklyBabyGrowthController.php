<?php

namespace App\Http\Controllers;

use App\Models\WeeklyBabyGrowth;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Storage;

class WeeklyBabyGrowthController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        $data = WeeklyBabyGrowth::with('user')->paginate(10);
        return view('babies.index', ['data' => $data]);
    }

    /**
     * Show the form for creating a new resource.
     */
    public function create()
    {
        return view('babies.create');
    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request)
    {
        $this->validate($request, [
            'title' => 'required|unique:weekly_baby_growths,title',
            'description' => 'nullable',
            'banner' => 'image',
            'references' => 'array',
            'week' => 'numeric|unique:weekly_baby_growths,week'
        ]);

        $imageName = $request->banner->getClientOriginalName();
        $path = $request->banner->storeAs('babies/banner', $imageName, 'public');
        $dbPath = 'storage/' . $path;

        $data = $request->only(['title', 'description', 'week']);

        $data['user_id'] = Auth::id();
        $data['banner_image'] = $dbPath;

        if(!empty($request->references)){
            $arrayReferences = [];
            foreach($request->references as $reference){
                $arrayReferences[] = $reference;
            }
            $data['references'] = ($arrayReferences);
        }

        WeeklyBabyGrowth::create($data);

        return redirect()->route('babies.index');
    }

    /**
     * Display the specified resource.
     */
    public function show(WeeklyBabyGrowth $weeklyBabyGrowth)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     */
    public function edit($id)
    {
        return view('babies.edit', ['data' => WeeklyBabyGrowth::find($id)]);
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, $id)
    {
        $weeklyBabyGrowth = WeeklyBabyGrowth::find($id);

        $this->validate($request, [
            'title' => 'unique:weekly_baby_growths,title,' . $weeklyBabyGrowth->id . ',id',
            'description' => 'nullable',
            'banner' => 'image'
        ]);

        $data = $request->only(['title', 'description']);

        if ($request->has('banner')) {
            if($weeklyBabyGrowth->banner_image !== null){
                Storage::delete($weeklyBabyGrowth->banner_image);
            }
            $imageName = $request->banner->getClientOriginalName();
            $path = $request->banner->storeAs('babies/banner', $imageName, 'public');
            $dbPath = 'storage/' . $path;
            $data['banner_image'] = $dbPath;
        }

        if(!empty($request->references)){
            $arrayReferences = [];
            foreach($request->references as $reference){
                $arrayReferences[] = $reference;
            }
            $data['references'] = json_encode($arrayReferences);
        }else{
            $data['references'] = null;
        }

        $data['week'] = $request->week;

        $weeklyBabyGrowth->update($data);

        return redirect()->route('babies.index');
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy($id)
    {
        WeeklyBabyGrowth::where('id', $id)->delete();
        return redirect()->route('babies.index');
    }
}
