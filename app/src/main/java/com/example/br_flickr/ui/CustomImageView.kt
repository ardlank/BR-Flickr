package com.example.br_flickr.ui

import android.content.Context
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.br_flickr.R

class CustomImageView : ConstraintLayout {
    lateinit var photoImage: ImageView
    lateinit var progressbar: ProgressBar
    lateinit var retryButton: Button

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        LayoutInflater.from(getContext()).inflate(R.layout.custom_image_view, this)

        photoImage = findViewById(R.id.photo_image)
        progressbar = findViewById(R.id.spinner)
        retryButton = findViewById(R.id.retry_button)
    }

    fun clearAll() {
        progressbar.visibility = View.GONE
        retryButton.visibility = View.GONE
    }

    fun showProgress() {
        retryButton.visibility = View.GONE
        progressbar.visibility = View.VISIBLE
    }

    fun showRetry() {
        progressbar.visibility = View.GONE
        retryButton.visibility = View.VISIBLE
    }
}