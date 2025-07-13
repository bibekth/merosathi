<aside>
    <div class="layout-sidenav custom-bg-light-blue shadow pt-5">
        <div class="sidenav-wrapper">
            <div class="sidenav-section px-4">
                <div class="side-nav-wrapper py-4">
                    <div class="logo d-flex justify-content-center">
                        <img src="{{ asset('assets/img/logo.png') }}" alt="Logo" height="160" width="auto">
                    </div>
                    <nav id="sidebarMenu" class="d-md-block sidebar collapse text-bold custom-text-gray">
                        <ul class="">
                            @hasrole('admin')
                            <li class="mb-3">
                                <div class="text-uppercase fs-5"><a href="{{ route('users.index') }}">Users</a></div>
                            </li>
                            <li class="mb-3">
                                <div class="text-uppercase fs-5"><a href="{{ route('doctors.index') }}">Doctors</a></div>
                            </li>
                            @endhasrole
                            <li class="mb-3">
                                <div class="text-uppercase fs-5"><a href="{{ route('articles.index') }}">Articles</a></div>
                            </li>
                            <li class="mb-3">
                                <div class="text-uppercase fs-5"><a href="{{ route('babies.index') }}">Baby Growth</a></div>
                            </li>
                            <li class="mb-3">
                                <div class="text-uppercase fs-5"><a href="{{ route('bodies.index') }}">Body Changes</a></div>
                            </li>
                        </ul>
                    </nav>
                </div>
            </div>
        </div>
    </div>
    <style>
        .layout-sidenav {
            position: fixed;
            left: 0;
            width: 16%;
            min-height: 100%;
            height: 100vh !important;
        }

        a{
            text-decoration: none;
            color: inherit;
        }

        li{
            list-style: none;
        }
    </style>
</aside>
