package com.example.br_flickr.View

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.br_flickr.Model.Photo
import com.example.br_flickr.R

class PhotoTileAdapter(val photos: List<Photo>): RecyclerView.Adapter<PhotoTileAdapter.ViewHolder>() {

    private lateinit var viewGroup: ViewGroup

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.photo_card, p0, false) as View
        viewGroup = p0
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        Glide
            .with(viewGroup.context)
            .load(photos[p1].url_t)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(getCircularProgress())
            .into(p0.photoImage);
        p0.title?.text = photos[p1].title
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photoImage = itemView.findViewById<ImageView>(R.id.photoImage)
        val title = itemView.findViewById<TextView>(R.id.title)
    }

    fun getCircularProgress() : CircularProgressDrawable {
        val circularProgressDrawable = CircularProgressDrawable(viewGroup.context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        return circularProgressDrawable
    }
}