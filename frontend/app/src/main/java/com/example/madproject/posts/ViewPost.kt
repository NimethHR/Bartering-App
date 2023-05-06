package com.example.madproject.posts

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.example.madproject.R
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ViewPost : AppCompatActivity() {

    private lateinit var documentId: String

    private lateinit var likeBtn: Button
    private lateinit var titleTextView: MaterialTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_post)

        documentId = intent.getStringExtra("documentId").toString()

        likeBtn = findViewById(R.id.likeBtn)

        likeBtn.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Confirm Delete")
            alertDialogBuilder.setMessage("Are you sure you want to delete this post?")

            //TODO - Add +ve and -ve options

            alertDialogBuilder.setPositiveButton("Delete") { dialog, which ->
                // Perform the delete operation here
                // Example: deleteItem()
            }
            alertDialogBuilder.setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }

    }
    private fun fetchData(){
        val db = Firebase.firestore

        val docRef = db.collection("posts").document(documentId)

        docRef.get()
            .addOnSuccessListener { result ->
                if (result.exists()){
                    val title = result.getString("title")
                    val desc = result.getString("description")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }
}