package com.telstra.feedapp.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.telstra.feedapp.R
import com.telstra.feedapp.models.NewsFeed
import kotlinx.android.synthetic.main.list_item_layout.view.*

class NewsFeedAdapter(private val feedList: List<NewsFeed>) :
    RecyclerView.Adapter<NewsFeedAdapter.NewsViewHolder>() {

    private val TAG: String = NewsFeedAdapter::class.java.simpleName

    private val requestOptions: RequestOptions = RequestOptions().error(R.drawable.ic_place_holder)
        .placeholder(R.drawable.ic_place_holder).fallback(R.drawable.ic_place_holder)

    private val requestListener = object : RequestListener<Drawable> {
        override fun onLoadFailed(
            error: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean
        ): Boolean {
            println("TAG --- $TAG --> ${error?.message}")
            return false
        }

        override fun onResourceReady(
            resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?
            , isFirstResource: Boolean
        ): Boolean = false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val customView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_layout, parent, false)
        return NewsViewHolder(customView)
    }

    override fun getItemCount(): Int = feedList.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        try {
            val data: NewsFeed = feedList[position]
            holder.tvTitle.text = data.getTitle()
            holder.tvDescription.text = data.getDescription()
            println("TAG --- $TAG --> ${data.getImageUrl()}")

            Glide.with(holder.ivImage.context)
                .load(data.getImageUrl())
                .listener(requestListener)
                .apply(requestOptions)
                .into(holder.ivImage)

        } catch (e: Exception) {
            println("TAG --- $TAG --> ${e.message}")
        }
    }

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.tvTitle
        val tvDescription: TextView = itemView.tvDescription
        val ivImage: ImageView = itemView.ivImage
    }
}