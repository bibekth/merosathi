<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Support\Facades\Auth;

class BodyChange extends Model
{
    protected $guarded = ['id'];

    protected $hidden = ['created_at', 'updated_at'];

    protected $casts = [
        'references' => 'array'
    ];

    public function user()
    {
        return $this->belongsTo(User::class);
    }

    public function liked()
    {
        return $this->hasMany(Like::class);
    }

    protected $appends = ['liked']; // automatically include in JSON

    public function getLikedAttribute()
    {
        $user = Auth::user();
        if (!$user) return false;

        return $this->liked()->where('user_id', $user->id)->exists();
    }
}
