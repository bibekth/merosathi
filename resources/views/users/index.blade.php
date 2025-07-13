@extends('layouts.app')

@section('content')
    <aside class="">
        <div class="">
            <div class="add-new mb-3">
                <a href="{{ route('users.create') }}"><button class="btn btn-sm btn-primary" id="add-new-user-btn"><i
                            class="bi bi-plus-circle-fill"></i><span>
                            Add New</span></button></a>
            </div>
        </div>
        <div class="list-users-section">
            <div class="table-section">
                <div class="table-responsive">
                    <table class="table table-bordered caption-top table-sm table-hover datatable">
                        {{-- <caption>List of users</caption> --}}
                        <thead class="thead">
                            <tr>
                                <th>S.N.</th>
                                <th>Name</th>
                                <th>Email</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody class="tbody">
                            @foreach ($data as $user)
                                <tr>
                                    <td>{{ $loop->iteration }}</td>
                                    <td>{{ $user->name }}</td>
                                    <td>{{ $user->email }}</td>
                                    <td>
                                        <div class="d-flex gap-1">
                                            <a href="{{ route('users.edit', $user->id) }}">
                                                <button type="button" class="btn btn-sm btn-secondary user-edit"
                                                    data-user-id="{{ $user->id }}">
                                                    Edit
                                                </button>
                                            </a>
                                            <form action="{{ route('users.destroy', $user->id) }}" method="POST"
                                                onsubmit="return confirm('Are you sure?');" class="d-inline">
                                                @csrf @method('DELETE')
                                                <button type="submit" class="btn btn-sm btn-danger user-delete"
                                                    data-user-id="{{ $user->id }}">
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
