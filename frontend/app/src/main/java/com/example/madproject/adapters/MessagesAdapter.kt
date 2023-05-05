package com.example.madproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.madproject.R
import com.example.madproject.fragments.GroupViewFragment
import com.example.madproject.fragments.NewGroupFragment
import com.example.madproject.models.Message

class MessagesAdapter(var messageList: List<Message>?): RecyclerView.Adapter<MessagesAdapter.MyViewHolder>() {

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val message: TextView =  view.findViewById(R.id.et_message_view_message)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessagesAdapter.MyViewHolder, position: Int) {
        holder.message.text = messageList?.get(position)?.content

    }

    override fun getItemCount(): Int {
        return messageList?.size ?: 0
    }
}