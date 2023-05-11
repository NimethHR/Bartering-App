package com.example.madproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.madproject.R
import com.example.madproject.fragments.GroupViewFragment
import com.example.madproject.util.StorageFunctions

class ChatActivityAdapter(var messageCountList: List<Pair<String, Long>>?): RecyclerView.Adapter<ChatActivityAdapter.MyViewHolder>() {

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val day = view.findViewById<TextView>(R.id.activity_day)
        val count = view.findViewById<TextView>(R.id.activity_count)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatActivityAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatActivityAdapter.MyViewHolder, position: Int) {
        holder.day.text = messageCountList?.get(position)?.first
        holder.count.text = messageCountList?.get(position)?.second.toString()
    }

    override fun getItemCount(): Int {
        return messageCountList?.size ?: 0
    }
}