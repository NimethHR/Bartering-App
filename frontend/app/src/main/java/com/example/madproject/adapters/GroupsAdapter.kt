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
import com.example.madproject.util.StorageFunctions

class GroupsAdapter(var groupList: List<Map<String, String>>?): RecyclerView.Adapter<GroupsAdapter.MyViewHolder>() {

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val name: TextView =  view.findViewById(R.id.group_name)
        val card = view.findViewById<View>(R.id.group_card)
        val imageView: ImageView = view.findViewById<ImageView>(R.id.iv_group_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupsAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.group_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupsAdapter.MyViewHolder, position: Int) {
        holder.name.text = groupList?.get(position)?.get("name")

        Glide.with(holder.itemView.context)
            .load(StorageFunctions.getGroupImageUrl(groupList?.get(position)?.get("id")!!))
            .circleCrop()
            .placeholder(R.drawable.baseline_groups_24)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .error(R.drawable.baseline_groups_24)
            .into(holder.imageView)

        holder.card.setOnClickListener(
            View.OnClickListener {
                val transaction = (holder.itemView.context as FragmentActivity).supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container, GroupViewFragment.newInstance(groupList?.get(position)?.get("id") ?: "0"))
                transaction.addToBackStack(null)
                transaction.commit()
            }
        )
    }

    override fun getItemCount(): Int {
        return groupList?.size ?: 0
    }
}