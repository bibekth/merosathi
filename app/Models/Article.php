<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Article extends Model
{
    protected $guarded = ['id'];

    protected $casts = [
        'references' => 'array'
    ];

    protected $hidden = ['created_at', 'updated_at'];

    protected function getBannerImageAttribute($value)
    {
        return $value ? config('app.url') . $value : null;
    }

    public function user()
    {
        return $this->belongsTo(User::class);
    }

    public function sections()
    {
        return $this->hasMany(ArticleSection::class);
    }
}
