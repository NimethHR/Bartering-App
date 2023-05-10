package com.example.madproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.madproject.R
import com.example.madproject.models.Feedback
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FeedbackAdp(
    private val feedbacks: ArrayList<Feedback>
) : RecyclerView.Adapter<FeedbackAdp.ViewHolder>(){

    val db = Firebase.firestore

    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val feedback : TextView = itemView.findViewById(R.id.feedback)
        val delete:Button = itemView.findViewById(R.id.deletefeed)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.feebback_card,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val feed = feedbacks[position]
        holder.feedback.text = feed.feedback
        holder.delete.setOnClickListener {
            val documentReference: DocumentReference = db.document("Feedbacks/"+feed.id);
            documentReference.delete().addOnSuccessListener {
                feedbacks.removeAt(position)
                notifyItemRemoved(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return feedbacks.size
    }

}