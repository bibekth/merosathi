<?php

namespace App\Http\Controllers;

use App\Models\Article;
use App\Models\ArticleSection;
use Illuminate\Http\Request;

class ArticleSectionController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index(Request $request)
    {
        $article = Article::find($request->article_id);
        $data = ArticleSection::where('article_id', $request->article_id)->paginate(10);
        return view('articles.section.index', ['data' => $data, 'item' => $article]);
    }

    /**
     * Show the form for creating a new resource.
     */
    public function create(Request $request)
    {
        $article = Article::find($request->article_id);
        return view('articles.section.create', ['item' => $article]);
    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request)
    {
        $this->validate($request, [
            'article' => 'required|exists:articles,title',
            'title' => 'required',
            'description' => 'required'
        ]);

        $article = Article::where('title', $request->article)->first();

        $data = $request->only(['title', 'description']);
        $data['article_id'] = $article->id;

        ArticleSection::create($data);

        return redirect()->route('article-sections.index', ['article_id' => $article->id]);
    }

    /**
     * Display the specified resource.
     */
    public function show(ArticleSection $articleSection)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     */
    public function edit(ArticleSection $articleSection)
    {
        return view('articles.section.edit', ['data' => $articleSection]);
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, ArticleSection $articleSection)
    {
        $this->validate($request, [
            'article' => 'required|exists:articles,title',
            'title' => 'required',
            'description' => 'nullable'
        ]);

        $data = $request->only(['title', 'description']);
        $data['article_id'] = $articleSection->article_id;

        $articleSection->update($data);

        return redirect()->route('article-sections.index', ['article_id' => $articleSection->article_id]);
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy(ArticleSection $articleSection)
    {
        $articleSection->delete();
        return back();
    }
}
