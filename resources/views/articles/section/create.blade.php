@extends('layouts.app')

@section('content')
    <aside class="">
        <div class="">
            <div class="add-new mb-3">
                <a href="{{ route('article-sections.index', ['article_id' => $item->id]) }}"><button class="btn btn-sm btn-primary" id="add-new-btn"><i
                            class="bi bi-plus-circle-fill"></i><span>
                            View Table</span></button></a>
            </div>
            <div class="add-form-section add-new-section" id="add-form-section">
                <form action="{{ route('article-sections.store') }}" class="form" method="POST"
                    enctype="multipart/form-data">
                    @csrf
                    <div class="row mb-3">
                        <div class="col-12">
                            <div class="mb-3">
                                <label for="article" class="form-label">For the Article:</label>
                                <input type="text" class="form-control @error('article') is-invalid @enderror"
                                    id="article" name="article" value="{{ $item->title }}" readonly>
                                @error('article')
                                    <div class="invalid-feedback">{{ $message }}</div>
                                @enderror
                            </div>
                        </div>

                        <div class="col-12">
                            <div class="mb-3">
                                <label for="title" class="form-label">Title:</label>
                                <input type="text" class="form-control @error('title') is-invalid @enderror"
                                    id="title" name="title" value="{{ old('title') }}"
                                    placeholder="Title of the article">
                                @error('title')
                                    <div class="invalid-feedback">{{ $message }}</div>
                                @enderror
                            </div>
                        </div>

                        <div class="col-12">
                            <div class="mb-3">
                                <label for="description" class="form-label">Description:</label>
                                <textarea name="description" id="description" cols="30" rows="5"
                                    class="form-control @error('description') is-invalid @enderror">{{ old('description') }}</textarea>
                                @error('description')
                                    <div class="invalid-feedback">{{ $message }}</div>
                                @enderror
                            </div>
                        </div>
                    </div>

                    <div class="row mb-3">
                        <div class="col-12">
                            <button class="btn btn-sm btn-primary col-12">Submit</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </aside>
@endsection
