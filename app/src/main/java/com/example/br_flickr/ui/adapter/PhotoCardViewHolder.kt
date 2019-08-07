package com.example.br_flickr.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.br_flickr.model.Photo
import com.example.br_flickr.R
import com.example.br_flickr.ui.PhotoDisplayActivity
import com.example.br_flickr.util.GlideRequests

//RecyclerView ViewHolder for a Photo Card
class PhotoCardViewHolder(view: View, private val glide: GlideRequests):
    RecyclerView.ViewHolder(view) {

    private val photoImage = itemView.findViewById<ImageView>(R.id.photoImage)
    private val title = itemView.findViewById<TextView>(R.id.title)
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
    }

    fun bind(photo: Photo?) {
        this.photo = photo
        title.text = photo?.title ?: "loading"
        glide
            .load(photo?.url_q)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(photoImage)
    }

    companion object {
        fun create(parent: ViewGroup, glide: GlideRequests): PhotoCardViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.photo_card, parent, false)
            return PhotoCardViewHolder(view, glide)
        }
    }

    fun updatePhoto(item: Photo?) {
        photo = item
        photo?.title = item?.title
    }

}