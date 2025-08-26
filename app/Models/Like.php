<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Like extends Model
{
    protected $guarded = ['id'];

    protected $hidden = ['created_at', 'updated_at'];

    public function user()
    {
        return $this->belongsTo(User::class);
    }

    public function article()
    {
        return $this->belongsTo(Article::class);
    }

    public function babyGrowth()
    {
        return $this->belongsTo(WeeklyBabyGrowth::class);
    }

    public function bodyChange()
    {
        return $this->belongsTo(BodyChange::class);
    }
}
