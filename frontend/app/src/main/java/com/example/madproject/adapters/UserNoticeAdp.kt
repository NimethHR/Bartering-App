package com.example.madproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.madproject.R
import com.example.madproject.models.Notice

class UserNoticeAdp(
    private val notices: ArrayList<Notice>
) : RecyclerView.Adapter<UserNoticeAdp.ViewHolder>() {

    class ViewHolder(noticeView:View):RecyclerView.ViewHolder(noticeView){
        val titleUser:TextView = noticeView.findViewById(R.id.UserTitle)
        val descriptionUser:TextView = noticeView.findViewById(R.id.userDescription)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val noticesView = LayoutInflater.from(parent.context).inflate(R.layout.user_notice_card, parent, false)
        return ViewHolder(noticesView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notices = notices[position]
        holder.titleUser.text = notices.title
        holder.descriptionUser.text = notices.description
    }

    override fun getItemCount(): Int {
        return notices.size
    }

}