package com.example.madproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.madproject.R
import com.example.madproject.models.Post
import com.example.madproject.models.PostUnit
import com.google.android.material.card.MaterialCardView

class PostAdapter(private val posts: List<PostUnit>) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    private var onItemClickListener: ((String) -> Unit)? = null

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.view_post_title)
//        val descTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
//        val quantityTextView: TextView = itemView.findViewById(R.id.quantityTextView)
        val postCard: MaterialCardView = itemView.findViewById(R.id.postCardView)
        val likesView: TextView = itemView.findViewById(R.id.likeCount)
        val imageView : ImageView = itemView.findViewById<ImageView>(R.id.imageView1)
    }

    fun setOnItemClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.post_view_unit, parent, false)
//        val lp = itemView.layoutParams
//        lp.width = parent.measuredWidth / 2

        return PostViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.titleTextView.text = post.title
//        holder.descTextView.text = post.description
//        holder.quantityTextView.text = post.quantity.toString()
//        holder.typeTextView.text = post.type
        holder.likesView.text = post.likes.toString()

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(post.documentId!!)
        }

        Glide.with(holder.itemView)
            .load(post.imageDownloadUrl)
            .placeholder(R.drawable.loading_bar)
            .into(holder.imageView)

    }

    override fun getItemCount(): Int {
        return posts.size
    }
}