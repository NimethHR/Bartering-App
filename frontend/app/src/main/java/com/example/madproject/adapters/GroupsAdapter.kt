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

class GroupsAdapter(var groupList: List<Map<String, String>>?): RecyclerView.Adapter<GroupsAdapter.MyViewHolder>() {

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val name: TextView =  view.findViewById(R.id.group_name)
        val card = view.findViewById<View>(R.id.group_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupsAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.group_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupsAdapter.MyViewHolder, position: Int) {
        holder.name.text = groupList?.get(position)?.get("name")
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