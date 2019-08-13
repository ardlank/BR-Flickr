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

    private lateinit var progressbar: ProgressBar

    private lateinit var photoView: ImageView

    private lateinit var retryButton: Button

    private val TAG = "PhotoDisplayActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photo_display)

        initComponents()
        setUpView()
        setupRetryListner()
    }

    private fun initComponents() {
        photoView = findViewById(R.id.photoImage)
        progressbar = findViewById(R.id.spinner)
        retryButton = findViewById(R.id.retry_button)
    }

    private fun setUpView() {
        if (intent != null) {
            val photoTitle = intent.extras?.get("photoTitle")
            val photoUrl = intent.extras?.get("photoUrl")
            if (photoTitle is String) setImageTitle(photoTitle)
            if (photoUrl is String) setImage(photoUrl)
        } else {
            showRetry()
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
                    showRetry()
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?,
                                             target: Target<Drawable>?, dataSource: DataSource?,
                                             isFirstResource: Boolean): Boolean {

                    clearAll()
                    return false
                }
            })
            .centerInside()
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .into(photoView)
    }

    private fun clearAll() {
        progressbar.visibility = View.GONE
        retryButton.visibility = View.GONE
    }

    private fun showProgress() {
        retryButton.visibility = View.GONE
        progressbar.visibility = View.VISIBLE
    }

    private fun showRetry(){
        progressbar.visibility = View.GONE
        retryButton.visibility = View.VISIBLE
    }

    private fun setupRetryListner() {
        retryButton.setOnClickListener {
            showProgress()
            setUpView()
        }
    }
}