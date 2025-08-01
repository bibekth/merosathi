@extends('layouts.app')

@section('content')
    <aside class="">
        <div class="">
            <div class="add-new mb-3">
                <a href="{{ route('bodies.index') }}"><button class="btn btn-sm btn-primary" id="add-new-user-btn"><i
                            class="bi bi-plus-circle-fill"></i><span>
                            View Table</span></button></a>
            </div>

            <div class="add-form-section add-new-section" id="add-form-section">
                <form action="{{ route('bodies.update', $data->id) }}" class="form" method="POST"
                    enctype="multipart/form-data">
                    @csrf @method('PATCH')
                    <div class="row mb-3">
                        <div class="col-12">
                            <div class="mb-3">
                                <label for="title" class="form-label">Title:</label>
                                <input type="text" class="form-control @error('title') is-invalid @enderror"
                                    id="title" name="title" value="{{ old('title') ?? $data->title }}"
                                    placeholder="Title of the article">
                                @error('title')
                                    <div class="invalid-feedback">{{ $message }}</div>
                                @enderror
                            </div>
                        </div>

                        <div class="col-12">
                            <div class="mb-3">
                                <label for="banner" class="form-label">Banner Image</label>
                                <input type="file" name="banner" id="banner"
                                    class="form-control @error('banner') is-invalid @enderror" value="">
                                @error('banner')
                                    <div class="invalid-feedback">{{ $message }}</div>
                                @enderror
                            </div>
                        </div>

                        <div class="col-12">
                            <div class="mb-3">
                                <label for="week" class="form-label">Week:</label>
                                <input type="number" class="form-control @error('week') is-invalid @enderror" min="0"
                                    id="week" name="week" value="{{ old('week') }}"
                                    placeholder="For the nth week.">
                                @error('week')
                                    <div class="invalid-feedback">{{ $message }}</div>
                                @enderror
                            </div>
                        </div>

                        <div class="col-12">
                            <div class="mb-3">
                                <label for="description" class="form-label">Description:</label>
                                <textarea name="description" id="description" cols="30" rows="5"
                                    class="form-control @error('description') is-invalid @enderror">{{ old('description') ?? $data->description }}</textarea>
                                @error('description')
                                    <div class="invalid-feedback">{{ $message }}</div>
                                @enderror
                            </div>
                        </div>
                    </div>

                    <div class="col-12">
                        <div class="mb-3">
                            <label class="form-label">References:</label>
                            <div id="reference-wrapper">
                                @if ($data->references !== null && count(json_decode($data->references)) > 0)
                                    @foreach (json_decode($data->references) as $item)
                                        <div class="input-group mb-2">
                                            <input type="text" name="references[]" class="form-control"
                                                value="{{ $item }}" placeholder="Enter reference">
                                            <button type="button"
                                                class="btn btn-outline-danger remove-reference">Remove</button>
                                        </div>
                                    @endforeach
                                @endif
                            </div>
                            <button type="button" class="btn btn-sm btn-outline-secondary" id="add-reference">Add
                                Reference</button>
                        </div>
                    </div>

                    <div class="row mb-3">
                        <div class="col-12">
                            <button class="btn btn-sm btn-primary col-12">Update</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <script>
            document.addEventListener('DOMContentLoaded', function() {
                const addButton = document.getElementById('add-reference');
                const wrapper = document.getElementById('reference-wrapper');

                addButton.addEventListener('click', function() {
                    const newInput = document.createElement('div');
                    newInput.classList.add('input-group', 'mb-2');
                    newInput.innerHTML = `
                <input type="text" name="references[]" class="form-control" placeholder="Enter reference">
                <button type="button" class="btn btn-outline-danger remove-reference">Remove</button>
            `;
                    wrapper.appendChild(newInput);
                });

                wrapper.addEventListener('click', function(e) {
                    if (e.target.classList.contains('remove-reference')) {
                        e.target.parentElement.remove();
                    }
                });
            });
        </script>
    </aside>
@endsection
