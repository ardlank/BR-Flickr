package com.example.br_flickr.ui.adapter

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.br_flickr.model.PhotoObject
import com.example.br_flickr.R
import com.example.br_flickr.source.local.FlickrDatabase
import com.example.br_flickr.util.GlideRequests
import com.example.br_flickr.util.NetworkState

//List Adapter for photo cards and network state.
class PhotoListAdapter(
    private val glide: GlideRequests,
    private val flickrDatabase: FlickrDatabase,
    private val retryCallback: () -> Unit
) :
    PagedListAdapter<PhotoObject, RecyclerView.ViewHolder>(POST_COMPARATOR) {

    private var networkState: NetworkState? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.photo_card -> (holder as PhotoCardViewHolder).bind(getItem(position))
            R.layout.network_state_item -> (holder as NetworkStateItemViewHolder).bind(networkState)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {

        if (payloads.isNotEmpty()) {
            val item = getItem(position)
            (holder as PhotoCardViewHolder).updatePhoto(item)
        } else {
            onBindViewHolder(holder, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.photo_card -> PhotoCardViewHolder.create(parent, glide, flickrDatabase)
            R.layout.network_state_item -> NetworkStateItemViewHolder.create(
                parent,
                retryCallback
            )
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.network_state_item
        } else {
            R.layout.photo_card
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    companion object {
        val POST_COMPARATOR = object : DiffUtil.ItemCallback<PhotoObject>() {
            override fun areContentsTheSame(oldItem: PhotoObject, newItem: PhotoObject): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: PhotoObject, newItem: PhotoObject): Boolean =
                oldItem.id == newItem.id
        }
    }
}