@extends('layouts.app')

@section('content')
    <aside class="">
        <div class="">
            <div class="add-new mb-3">
                @hasrole('doctor')
                    <a href="{{ route('articles.create') }}"><button class="btn btn-sm btn-primary" id="add-new-btn"><i
                                class="bi bi-plus-circle-fill"></i><span>
                                Add New</span></button></a>
                @else
                    <div class=""><span class="text-underline">
                            The Articles</span></div>
                @endhasrole
            </div>
        </div>
        <div class="list-section">
            <div class="table-section">
                <div class="table-responsive">
                    <table class="table table-bordered caption-top table-sm table-hover datatable">
                        <thead class="thead">
                            <tr>
                                <th>S.N.</th>
                                <th>Article By</th>
                                <th>Title</th>
                                <th>Description</th>
                                <th>Image</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody class="tbody">
                            @foreach ($data as $item)
                                <tr>
                                    <td>{{ $loop->iteration }}</td>
                                    <td>{{ $item->user->name }}</td>
                                    <td>{{ $item->title }}</td>
                                    <td data-bs-toggle="tooltip" data-bs-placement="top" title="{{ $item->description }}">
                                        {{ \Illuminate\Support\Str::limit($item->description, 50) }}
                                    </td>
                                    <td>
                                        <a href="{{ asset($item->banner_image) }}" target="__blank" title="Click to preview">
                                            <img src="{{ asset($item->banner_image) }}" alt="" height="30"
                                                width="auto" />
                                        </a>
                                    </td>
                                    <td>
                                        <div class="d-flex gap-1">
                                            <a href="{{ route('article-sections.index', ['article_id' => $item->id]) }}">
                                                <button type="button" class="btn btn-sm btn-primary item-edit"
                                                    data-item-id="{{ $item->id }}">
                                                    Sections
                                                </button>
                                            </a>
                                            @hasrole('doctor')
                                                <a href="{{ route('articles.edit', $item->id) }}">
                                                    <button type="button" class="btn btn-sm btn-secondary item-edit"
                                                        data-item-id="{{ $item->id }}">
                                                        Edit
                                                    </button>
                                                </a>
                                            @endhasrole
                                            <form action="{{ route('articles.destroy', $item->id) }}" method="POST"
                                                onsubmit="return confirm('Are you sure?');" class="d-inline">
                                                @csrf @method('DELETE')
                                                <button type="submit" class="btn btn-sm btn-danger item-delete"
                                                    data-item-id="{{ $item->id }}">
                                                    Delete
                                                </button>
                                            </form>
                                        </div>
                                    </td>
                                </tr>
                            @endforeach
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </aside>
@endsection
