<aside>
    <div class="layout-sidenav">
        <div class="sidenav-wrapper">
            <div class="sidenav-section">
                <div class="logo-section p-5 d-flex justify-content-center">
                    <a href="#">
                        <h4 class="text-uppercase">{{ config('app.name') }}</h4>
                    </a>
                </div>
                <div class="side-nav-wrapper">
                    <ul>
                        <li class="mb-3">
                            <div class="vendor-section text-uppercase">Vendor Section</div>
                            <ul>
                                <li><a href="#">Add Vendor</a></li>
                                <li><a href="#">List Vendor</a></li>
                            </ul>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
    <style>
        .layout-sidenav {
            position: absolute;
            left: 0;
            top: 0;
            width: 20%;
            height: 100%;
            background-color: #D3D3D3;
        }

        .logo-section {
            position: relative;
            top: 0;
            left: 0;
        }
    </style>
</aside>
