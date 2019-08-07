package com.example.br_flickr.ui

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.br_flickr.R

//Display image View
class PhotoDisplayActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photo_display)
        val imageView = this.findViewById<ImageView>(R.id.photoImage)

        if(intent != null) {
            val photoTitle = intent.extras?.get("photoTitle")
            val photoUrl = intent.extras?.get("photoUrl")
            if(photoTitle is String) supportActionBar?.title = photoTitle
            Glide
                .with(this)
                .load(photoUrl)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView)
        }
    }
}