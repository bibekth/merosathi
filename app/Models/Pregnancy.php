<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Pregnancy extends Model
{
    protected $table = 'pregencies';

    protected $hidden = ['created_at', 'updated_at'];
}
