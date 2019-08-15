package com.example.br_flickr.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
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
class PhotoDisplayActivity : AppCompatActivity() {

    private lateinit var customImageView: CustomImageView
    private var photoTitle = ""
    private var photoUrl = ""

    private val TAG = "PhotoDisplayActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photo_display)

        setupCustomView()
        getPhoto()
        setUpView()
        setupRetryListner()
    }

    private fun setupCustomView() {
        customImageView = findViewById(R.id.custom_image_view)
    }

    private fun getPhoto() {
        if (intent != null) {
            photoTitle = intent.extras?.get("photoTitle") as String
            photoUrl = intent.extras?.get("photoUrl") as String
        }
    }

    private fun setUpView() {
        if(photoTitle.isNotEmpty() && photoUrl.isNotEmpty()) {
            setImageTitle(photoTitle)
            setImage(photoUrl)
        } else {
            customImageView.showRetry()
        }
    }

    private fun setImageTitle(title: String) {
        supportActionBar?.title = title
    }

    private fun setImage(photoUrl: String?) {
        Glide
            .with(this)
            .load(photoUrl)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?,
                                          target: Target<Drawable>?,
                                          isFirstResource: Boolean): Boolean {
                    Log.e(TAG, "Load failed", e);
                    customImageView.showRetry()
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?,
                                             target: Target<Drawable>?, dataSource: DataSource?,
                                             isFirstResource: Boolean): Boolean {

                    customImageView.clearAll()
                    return false
                }
            })
            .centerInside()
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .into(customImageView.photoImage)
    }

    private fun setupRetryListner() {
        customImageView.retryButton.setOnClickListener {
            customImageView.showProgress()
            if(photoTitle.isEmpty() or photoUrl.isEmpty()) {
                getPhoto()
            }
            setUpView()
        }
    }
}