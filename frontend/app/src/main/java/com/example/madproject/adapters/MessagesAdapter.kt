package com.example.madproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.madproject.R
import com.example.madproject.fragments.GroupViewFragment
import com.example.madproject.fragments.NewGroupFragment
import com.example.madproject.models.Message
import com.example.madproject.util.StorageFunctions

class MessagesAdapter(var messageList: List<Message>?): RecyclerView.Adapter<MessagesAdapter.MyViewHolder>() {

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val message: TextView =  view.findViewById(R.id.et_message_view_message)
        val image: ImageView = view.findViewById<ImageView>(R.id.iv_message_view_image)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessagesAdapter.MyViewHolder, position: Int) {
        holder.message.text = messageList?.get(position)?.content

        Glide.with(holder.itemView.context)
            .load(StorageFunctions.getUserImageUrl(messageList?.get(position)?.senderId!!))
            .circleCrop()
            .placeholder(R.mipmap.default_profile_image)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .error(R.mipmap.default_profile_image)
            .into(holder.image)

    }

    override fun getItemCount(): Int {
        return messageList?.size ?: 0
    }
}