package com.example.martian.photos.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.martian.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MarsPhotosListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mars_photos_list)
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, MarsPhotoListFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }
}