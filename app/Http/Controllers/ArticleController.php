<?php

namespace App\Http\Controllers;

use App\Models\Article;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Storage;
use Throwable;

class ArticleController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        $auth = User::find(Auth::id());
        if ($auth->hasrole('admin')) {
            $data = Article::with('user')->paginate(10);
        } elseif ($auth->hasrole('doctor')) {
            $data = Article::with('user')->where('user_id', $auth->id)->paginate(2);
        }
        return view('articles.index', ['data' => $data]);
    }

    /**
     * Show the form for creating a new resource.
     */
    public function create()
    {
        return view('articles.create');
    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request)
    {
        $this->validate($request, [
            'title' => 'required|unique:articles,title',
            'description' => 'nullable',
            'banner' => 'image',
            'references' => 'array',
        ]);

        $imageName = $request->banner->getClientOriginalName();
        $path = $request->banner->storeAs('articles/banner', $imageName, 'public');
        $dbPath = 'storage/' . $path;

        $data = $request->only(['title', 'description']);

        $data['user_id'] = Auth::id();
        $data['banner_image'] = $dbPath;

        if(!empty($request->references)){
            $arrayReferences = [];
            foreach($request->references as $reference){
                $arrayReferences[] = $reference;
            }
            $data['references'] = $arrayReferences;
        }

        Article::create($data);

        return redirect()->route('articles.index');
    }

    /**
     * Display the specified resource.
     */
    public function show(Article $article)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     */
    public function edit(Article $article)
    {
        return view('articles.edit', ['data' => $article]);
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, Article $article)
    {
        $this->validate($request, [
            'title' => 'unique:articles,title,' . $article->id . ',id',
            'description' => 'nullable',
            'banner' => 'image'
        ]);

        $data = $request->only(['title', 'description']);

        if ($request->has('banner')) {
            if($article->banner_image !== null){
                Storage::delete($article->banner_image);
            }
            $imageName = $request->banner->getClientOriginalName();
            $path = $request->banner->storeAs('articles/banner', $imageName, 'public');
            $dbPath = 'storage/' . $path;
            $data['banner_image'] = $dbPath;
        }

        if(!empty($request->references)){
            $arrayReferences = [];
            foreach($request->references as $reference){
                $arrayReferences[] = $reference;
            }
            $data['references'] = ($arrayReferences);
        }else{
            $data['references'] = null;
        }

        $article->update($data);

        return redirect()->route('articles.index');
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy(Article $article)
    {
        $article->delete();
        return redirect()->route('articles.index');
    }
}
