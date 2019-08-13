package com.example.br_flickr.ui.adapter

import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.br_flickr.model.Photo
import com.example.br_flickr.R
import com.example.br_flickr.source.local.FlickrDatabase
import com.example.br_flickr.ui.PhotoDisplayActivity
import com.example.br_flickr.util.GlideRequests
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//RecyclerView ViewHolder for a Photo Card
class PhotoCardViewHolder(view: View, private val glide: GlideRequests, private val flickrDatabase: FlickrDatabase) :
    RecyclerView.ViewHolder(view) {

    private val photoImage = view.findViewById<ImageView>(R.id.photoImage)
    private val title = view.findViewById<TextView>(R.id.title)
    private val bookmark = view.findViewById<ImageButton>(R.id.bookmark)
    private val progressbar = view.findViewById<ProgressBar>(R.id.spinner)
    private val retryButton = view.findViewById<Button>(R.id.retry_button)
    private var photo: Photo? = null

    private val TAG = "PhotoCardViewHolder"

    init {
        itemView.isClickable = false
        itemView.setOnClickListener {
            val intent = Intent(view.context, PhotoDisplayActivity::class.java).apply {
                putExtra("photoUrl", photo?.url_m)
                putExtra("photoTitle", photo?.title)
            }
            view.context.startActivity(intent)
        }
        bookmark.setOnClickListener {
            setBookmark()
        }
        retryButton.setOnClickListener {
            showProgress()
            bind(photo)
        }
    }

    private fun setBookmarkIcon() {
        if (photo?.isBookmarked == true) bookmark.setImageResource(R.drawable.ic_bookmark_filled)
        else bookmark.setImageResource(R.drawable.ic_bookmark)
    }

    private fun setBookmark(){
        if (photo?.isBookmarked == true) {
            photo?.isBookmarked = false
            CoroutineScope(Dispatchers.IO).launch {
                flickrDatabase.removeBookmark(photo)
            }
        } else {
            photo?.isBookmarked = true
            CoroutineScope(Dispatchers.IO).launch {
                flickrDatabase.bookmarkPhoto(photo)
            }
        }
        setBookmarkIcon()
    }

    private fun setImage() {
        glide
            .load(photo?.url_n)
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
            .into(photoImage)
    }

    fun bind(photo: Photo?) {
        this.photo = photo
        setBookmarkIcon()
        title.text = photo?.title ?: "loading"
        setImage()
    }

    companion object {
        fun create(parent: ViewGroup, glide: GlideRequests, flickrDatabase: FlickrDatabase): PhotoCardViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.photo_card, parent, false)
            return PhotoCardViewHolder(view, glide, flickrDatabase)
        }
    }

    fun updatePhoto(item: Photo?) {
        bind(photo)
    }

    private fun clearAll() {
        itemView.isClickable = true
        progressbar.visibility = View.GONE
        retryButton.visibility = View.GONE
    }

    private fun showProgress() {
        retryButton.visibility = View.GONE
        progressbar.visibility = View.VISIBLE
    }

    private fun showRetry(){
        itemView.isClickable = false
        progressbar.visibility = View.GONE
        retryButton.visibility = View.VISIBLE
    }

}