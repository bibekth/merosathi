<?php

namespace App\Http\Middleware;

use App\Models\User;
use Closure;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Symfony\Component\HttpFoundation\Response;

class CheckRole
{
    /**
     * Handle an incoming request.
     *
     * @param  \Closure(\Illuminate\Http\Request): (\Symfony\Component\HttpFoundation\Response)  $next
     */
     public function handle(Request $request, Closure $next, ...$roles): Response
    {
        $auth = User::find(Auth::id());

        if (!$auth) {
            return abort(401);
        }

        foreach($roles as $role){
            if($auth->hasRole($role)){
                return $next($request);
            }
        }

        return abort(403);
    }
}
