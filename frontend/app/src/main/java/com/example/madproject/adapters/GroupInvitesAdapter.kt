package com.example.madproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.madproject.R
import com.example.madproject.fragments.GroupInvitesFragment
import com.example.madproject.fragments.GroupListFragment
import com.example.madproject.fragments.GroupViewFragment
import com.example.madproject.fragments.NewGroupFragment
import com.example.madproject.models.GroupInvite
import com.example.madproject.util.DatabaseFunctions
import com.example.madproject.util.StorageFunctions
import org.w3c.dom.Text

class GroupInvitesAdapter(var groupInvites: List<GroupInvite>): RecyclerView.Adapter<GroupInvitesAdapter.MyViewHolder>() {

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val name: TextView =  view.findViewById(R.id.tv_group_invite_name)
        val inviter: TextView = view.findViewById(R.id.tv_group_invite_inviter)
        val acceptBtn: Button = view.findViewById(R.id.btn_group_invite_accept)
        val declineBtn: Button = view.findViewById(R.id.btn_group_invite_reject)
        val imageView: ImageView = view.findViewById(R.id.iv_group_invite_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupInvitesAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.group_invite_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupInvitesAdapter.MyViewHolder, position: Int) {
        holder.name.text = groupInvites[position].groupName
        holder.inviter.text = "invited by: ${groupInvites[position].inviterName}"

        Glide.with(holder.itemView.context)
            .load(StorageFunctions.getGroupImageUrl(groupInvites[position].groupId!!))
            .circleCrop()
            .placeholder(R.drawable.baseline_groups_24)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(holder.imageView)

        holder.acceptBtn.setOnClickListener() {
            DatabaseFunctions.acceptGroupInvite(groupInvites[position].groupId!!, groupInvites[position].groupName!!) {
                val fragment = GroupListFragment() // replace MyNewFragment with the desired fragment class
                val fragmentManager = (holder.itemView.context as AppCompatActivity).supportFragmentManager
                fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit()
            }
        }

        holder.declineBtn.setOnClickListener() {
            DatabaseFunctions.declineGroupInvite(groupInvites[position].groupId!!) {
                val fragment = GroupListFragment() // replace MyNewFragment with the desired fragment class
                val fragmentManager = (holder.itemView.context as AppCompatActivity).supportFragmentManager
                fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit()
            }
        }
    }

    override fun getItemCount(): Int {
        return groupInvites.size
    }
}