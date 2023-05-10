package com.example.madproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.example.madproject.R
import com.example.madproject.models.Notice
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class NoticeAdp(
    private val notices: ArrayList<Notice>
) : RecyclerView.Adapter<NoticeAdp.ViewHolder>() {

    val db = Firebase.firestore

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val titleTextView: EditText= itemView.findViewById(R.id.feedback)
        val descriptionTextView: EditText= itemView.findViewById(R.id.descriptionTextView)
        var button: Button = itemView.findViewById(R.id.delete)
        var update: Button = itemView.findViewById(R.id.updateNotice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.notice_card, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notice = notices[position]
        holder.titleTextView.setText(notice.title)
        holder.descriptionTextView.setText(notice.description)
        holder.button.setOnClickListener {
            val documentReference: DocumentReference = db.document("Notices/"+notice.id);
            documentReference.delete().addOnSuccessListener {
                notices.removeAt(position)
                notifyItemRemoved(position)
            }
        }
        holder.update.setOnClickListener {
            db.collection("Notices").document(notice.id)
                .update("title",holder.titleTextView.text.toString(),"description",holder.descriptionTextView.text.toString())
        }
    }

    override fun getItemCount() = notices.size
}