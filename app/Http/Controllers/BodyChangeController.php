<?php

namespace App\Http\Controllers;

use App\Models\BodyChange;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Storage;

class BodyChangeController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        $data = BodyChange::paginate(10);
        return view('bodies.index', ['data' => $data]);
    }

    /**
     * Show the form for creating a new resource.
     */
    public function create()
    {
        return view('bodies.create');
    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request)
    {
        $this->validate($request, [
            'title' => 'required|unique:body_changes,title',
            'description' => 'nullable',
            'banner' => 'image',
            'references' => 'array',
            'week' => 'required|numeric|unique:body_changes,week'
        ]);

        $imageName = $request->banner->getClientOriginalName();
        $path = $request->banner->storeAs('bodies/banner', $imageName, 'public');
        $dbPath = 'storage/' . $path;

        $data = $request->only(['title', 'description', 'week']);

        $data['user_id'] = Auth::id();
        $data['banner_image'] = $dbPath;

        if (!empty($request->references)) {
            $arrayReferences = [];
            foreach ($request->references as $reference) {
                $arrayReferences[] = $reference;
            }
            $data['references'] = ($arrayReferences);
        }

        BodyChange::create($data);

        return redirect()->route('bodies.index');
    }

    /**
     * Display the specified resource.
     */
    public function show(BodyChange $bodyChange)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     */
    public function edit($id)
    {
        return view('bodies.edit', ['data' => BodyChange::find($id)]);
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, $id)
    {
        $bodyChange = BodyChange::find($id);

        $this->validate($request, [
            'title' => 'unique:weekly_baby_growths,title,' . $bodyChange->id . ',id',
            'description' => 'nullable',
            'banner' => 'image',
            'week' => 'numeric'
        ]);

        $data = $request->only(['title', 'description']);

        if ($request->has('banner')) {
            if ($bodyChange->banner_image !== null) {
                Storage::delete($bodyChange->banner_image);
            }
            $imageName = $request->banner->getClientOriginalName();
            $path = $request->banner->storeAs('bodies/banner', $imageName, 'public');
            $dbPath = 'storage/' . $path;
            $data['banner_image'] = $dbPath;
        }

        if (!empty($request->references)) {
            $arrayReferences = [];
            foreach ($request->references as $reference) {
                $arrayReferences[] = $reference;
            }
            $data['references'] = json_encode($arrayReferences);
        } else {
            $data['references'] = null;
        }

        $data['week'] = $request->week;

        $bodyChange->update($data);

        return redirect()->route('bodies.index');
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy($id)
    {
        BodyChange::where('id', $id)->delete();
        return redirect()->route('bodies.index');
    }
}
