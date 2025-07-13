<?php

namespace Database\Seeders;

use App\Models\Article;
use App\Models\ArticleSection;
use App\Models\BodyChange;
use App\Models\Doctor;
use App\Models\NormalPeople;
use App\Models\User;
use App\Models\WeeklyBabyGrowth;
// use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\Hash;
use Spatie\Permission\Models\Role;

class DatabaseSeeder extends Seeder
{
    /**
     * Seed the application's database.
     */
    public function run(): void
    {
        Role::insert([
            ['name' => 'admin', 'guard_name' => 'web'],
            ['name' => 'doctor', 'guard_name' => 'web'],
            ['name' => 'user', 'guard_name' => 'web'],
        ]);

        $admin = User::create(['name' => 'Mero Sathi Admin', 'email' => 'admin@merosathi.com', 'password' => Hash::make('password')]);
        $doctor = User::create(['name' => 'Mero Sathi Doctor', 'email' => 'doctor@merosathi.com', 'password' => Hash::make('password')]);
        $user = User::create(['name' => 'Mero Sathi User', 'email' => 'user@merosathi.com', 'password' => Hash::make('password')]);

        $admin->assignRole('admin');
        $doctor->assignRole('doctor');
        $user->assignRole('user');

        Doctor::create(['user_id' => $doctor->id, 'name' => $doctor->name, 'email' => $doctor->email, 'contact' => '9800000000', 'address' => 'Bharatpur, Chitwan', 'description' => 'Professor of obstetrics and gynecology, Tribhuwan University, Nepal. 10+ years in obstetrics and gynecology']);
        NormalPeople::create(['user_id' => $user->id, 'name' => $user->name, 'username' => 'mero_sathi_user', 'email' => $user->email, 'contact' => '9800001000', 'dob' => '1998-05-23']);

        $article = Article::create(['user_id' => $doctor->id, 'title' => 'When does an embryo bacome a fetus?', 'description' => "If there's a time we wish we'd paid more attention in high school biology, it's during pregnancy. Whether it's mind-blowing path to conception, or unfamiliar medical terms we need to keep looking up online, pregnancy brings up a long list of questions we'd never really considered until we were pregnant."]);
        ArticleSection::create(['article_id' => $article->id, 'title' => 'Back to basics', 'description' => "Way before your baby is a bundle of joy in a buggy, its journey begins as an embryo. And to amke one of those, you need that magical moment to happen - or, in medical terms, fertilization."]);

        WeeklyBabyGrowth::create(['user_id' => $doctor->id, 'title' => 'How your pregnancy begins', 'description' => 'Although this week is considered to be first week of pregnancy, you actually had your period at the time.']);

        BodyChange::create(['user_Id' => $doctor->id, 'title' => '3 weeks: Fertilization & implantation', 'description' => "You might be aware of classic pregnancy symptoms like nausea and a missed period, but how can you tell if you're pregnant after what's considered your 3rd week of pregnancy?"]);
    }
}
