<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class BodyChange extends Model
{
    protected $guarded = ['id'];

    protected $casts = [
        'references' => 'array'
    ];

    public function user()
    {
        return $this->belongsTo(User::class);
    }
}
