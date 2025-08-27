<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Support\Facades\Auth;

class Article extends Model
{
    protected $guarded = ['id'];

    protected $casts = [
        'references' => 'array'
    ];

    protected $hidden = ['created_at', 'updated_at'];

    public function user()
    {
        return $this->belongsTo(User::class);
    }

    public function sections()
    {
        return $this->hasMany(ArticleSection::class);
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
