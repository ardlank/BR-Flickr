package com.example.br_flickr.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.br_flickr.R

//Display image View
class PhotoDisplayActivity: AppCompatActivity() {

    private lateinit var progressbar: ProgressBar

    private lateinit var photoView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photo_display)

        setUpView()
    }

    fun setUpView() {
        photoView = findViewById(R.id.photoImage)
        progressbar = findViewById(R.id.spinner)
        if(intent != null) {
            val photoTitle = intent.extras?.get("photoTitle")
            val photoUrl = intent.extras?.get("photoUrl")
            if(photoTitle is String) setImageTitle(photoTitle)
            if(photoUrl is String) setImage(photoUrl)
        }
    }

    fun setImageTitle(title: String) {
        supportActionBar?.title = title
    }

    fun setImage(photoUrl: String?) {
        Glide
            .with(this)
            .load(photoUrl)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?,
                                          isFirstResource: Boolean): Boolean {
                    onFinished()
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?,
                                             dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    onFinished()
                    return false
                }
            })
            .centerInside()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(photoView)
    }

    fun onFinished(){
        if (progressbar != null) {
            progressbar.visibility = View.GONE
        }
    }
}