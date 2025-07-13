@extends('layouts.app')

@section('content')
    <aside class="">
        <div class="">
            <div class="add-new mb-3">
                <a href="{{ route('doctors.index') }}"><button class="btn btn-sm btn-primary" id="add-new-user-btn"><i
                            class="bi bi-plus-circle-fill"></i><span>
                            View Table</span></button></a>
            </div>
            <div class="add-form-section add-new-section" id="add-form-section">
                <form action="{{ route('doctors.update', $data->id) }}" class="form" method="POST" enctype="multipart/form-data">
                    @csrf @method('PATCH')
                    <div class="row mb-3">
                        <div class="col-6">
                            <div class="mb-3">
                                <label for="name" class="form-label">Name:</label>
                                <input type="text" class="form-control @error('name') is-invalid @enderror"
                                    id="name" name="name" value="{{ old('name') ?? $data->name }}"
                                    placeholder="Name of the user">
                                @error('name')
                                    <div class="invalid-feedback">{{ $message }}</div>
                                @enderror
                            </div>

                            <div class="mb-3">
                                <label for="email" class="form-label">Email:</label>
                                <input type="text" class="form-control @error('email') is-invalid @enderror"
                                    id="email" name="email" value="{{ old('email') ?? $data->email }}"
                                    placeholder="Email of the user">
                                @error('email')
                                    <div class="invalid-feedback">{{ $message }}</div>
                                @enderror
                            </div>
                        </div>

                        <div class="col-6">
                            <div class="mb-3">
                                <label for="password" class="form-label">Password:</label>
                                <input type="password" class="form-control @error('password') is-invalid @enderror"
                                    id="password" name="password" placeholder="Password">
                                @error('password')
                                    <div class="invalid-feedback">{{ $message }}</div>
                                @enderror
                            </div>

                            <div class="mb-3">
                                <label for="confirm_password" class="form-label">Confirm Password:</label>
                                <input type="password" class="form-control @error('confirm_password') is-invalid @enderror"
                                    id="confirm_password" name="confirm_password" placeholder="Confirm Password">
                                @error('confirm_password')
                                    <div class="invalid-feedback">{{ $message }}</div>
                                @enderror
                            </div>
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
    </aside>
@endsection
