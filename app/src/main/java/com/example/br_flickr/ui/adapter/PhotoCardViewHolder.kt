package com.example.br_flickr.ui.adapter

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.br_flickr.model.Photo
import com.example.br_flickr.R
import com.example.br_flickr.source.local.FlickrDB
import com.example.br_flickr.ui.PhotoDisplayActivity
import com.example.br_flickr.util.GlideRequests
import kotlinx.android.synthetic.main.photo_card.view.*

//RecyclerView ViewHolder for a Photo Card
class PhotoCardViewHolder(view: View, private val glide: GlideRequests, private val flickrDB: FlickrDB):
    RecyclerView.ViewHolder(view) {

    private val photoImage = itemView.findViewById<ImageView>(R.id.photoImage)
    private val title = itemView.findViewById<TextView>(R.id.title)
    private val bookmark = itemView.findViewById<Button>(R.id.bookmark)
    private var photo : Photo? = null

    init {
        view.setOnClickListener {
            photo?.id.let { id ->
                val intent = Intent(view.context, PhotoDisplayActivity::class.java).apply {
                    putExtra("photoUrl", photo?.url_m)
                    putExtra("photoTitle", photo?.title)
                }
                view.context.startActivity(intent)
            }
        }
        bookmark.setOnClickListener {
            if(photo?.isBookmarked == true) {
                photo?.isBookmarked = false
                flickrDB.posts().delete(photo?.id)
            }
            else {
                photo?.isBookmarked = true
                flickrDB.posts().insert(photo)
            }
            setBookmarkView()
        }
    }

    private fun setBookmarkView(){
        if(photo?.isBookmarked == true) bookmark.setBackgroundColor(Color.RED)
        else bookmark.setBackgroundColor(Color.BLUE)
    }

    fun bind(photo: Photo?) {
        if(flickrDB.posts().findId(photo?.id) != null) photo?.isBookmarked = true
        else photo?.isBookmarked = false

        this.photo = photo
        title.text = photo?.title ?: "loading"
        setBookmarkView()
        glide
            .load(photo?.url_n)
            .centerInside()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(photoImage)
    }

    companion object {
        fun create(parent: ViewGroup, glide: GlideRequests, flickrDB: FlickrDB): PhotoCardViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.photo_card, parent, false)
            return PhotoCardViewHolder(view, glide, flickrDB)
        }
    }

    fun updatePhoto(item: Photo?) {
        photo = item
        photo?.title = item?.title
    }

}