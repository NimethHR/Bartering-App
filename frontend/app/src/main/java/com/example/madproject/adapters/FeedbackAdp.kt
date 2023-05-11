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

    class ViewHolder(fitemView:View):RecyclerView.ViewHolder(fitemView){
        val feedback : TextView = fitemView.findViewById(R.id.feedback1)
        val delete:Button = fitemView.findViewById(R.id.deletefeed)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {
        val fitemView = LayoutInflater.from(parent.context).inflate(R.layout.feebback_card,parent,false)
        return ViewHolder(fitemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val feed = feedbacks[position]
        holder.feedback.setText(feed.feedback)
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