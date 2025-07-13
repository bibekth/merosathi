@extends('layouts.app')

@section('content')
    <aside class="">
        <div class="">
            <div class="add-new mb-3">
                @hasrole('doctor')
                    <a href="{{ route('article-sections.create', ['article_id' => $item->id]) }}"><button class="btn btn-sm btn-primary" id="add-new-btn"><i
                                class="bi bi-plus-circle-fill"></i><span>
                                Add New</span></button></a>
                @else
                    <div class=""><span>
                            The Sections of <strong><span class="text-underline">{{ $item->title }}</span></strong></span></div>
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
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody class="tbody">
                            @foreach ($data as $item)
                                <tr>
                                    <td>{{ $loop->iteration }}</td>
                                    <td>{{ $item->article->user->name }}</td>
                                    <td>{{ $item->title }}</td>
                                    <td data-bs-toggle="tooltip" data-bs-placement="top" title="{{ $item->description }}">
                                        {{ \Illuminate\Support\Str::limit($item->description, 50) }}
                                    </td>
                                    <td>
                                        <div class="d-flex gap-1">
                                            @hasrole('doctor')
                                                <a href="{{ route('article-sections.edit', $item->id) }}">
                                                    <button type="button" class="btn btn-sm btn-secondary item-edit"
                                                        data-item-id="{{ $item->id }}">
                                                        Edit
                                                    </button>
                                                </a>
                                            @endhasrole
                                            <form action="{{ route('article-sections.destroy', $item->id) }}" method="POST"
                                                onsubmit="return confirm('Are you sure?');" class="d-inline">
                                                @csrf @method('DELETE')
                                                <button type="submit" class="btn btn-sm btn-danger item-delete"
                                                    data-item-id="{{ $item->id }}">
                                                    Delete
                                                </button>
                                            </form>
                                        </div>
                                    </td>
                                    <div class="modal fade" id="viewModal-{{ $item->id }}" tabindex="-1"
                                        role="dialog" aria-labelledby="viewBannerLabel" aria-hidden="true">
                                        <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <h5 class="modal-title" id="viewBannerLabel">{{ $item->name }}</h5>
                                                    <button type="button" class="close" data-dismiss="modal"
                                                        aria-label="Close">
                                                        <span aria-hidden="true">&times;</span>
                                                    </button>
                                                </div>
                                                <div class="modal-body">
                                                    <img src="{{ asset($item->image) }}" alt="{{ $item->name }}"
                                                        class="img-fluid">
                                                </div>
                                                <div class="modal-footer">
                                                    <button type="button" class="btn btn-secondary"
                                                        data-dismiss="modal">Close</button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </tr>
                            @endforeach
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </aside>
@endsection
