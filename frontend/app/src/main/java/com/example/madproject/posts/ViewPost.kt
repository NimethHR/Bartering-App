package com.example.madproject.posts

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.example.madproject.MainActivity
import com.example.madproject.R
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ViewPost : AppCompatActivity() {

    private  var documentId: String? = null

    private lateinit var likeBtn: Button
    private lateinit var titleTextView: MaterialTextView
    private lateinit var descTextView: MaterialTextView
    private lateinit var viewPosType: MaterialTextView

    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_post)

        documentId = intent.getStringExtra("documentId").toString()

        Log.d("Tag", "document id in ViewPost: $documentId")

        val toolbar = findViewById<Toolbar>(R.id.create_toolbar)
        setSupportActionBar(toolbar)

        likeBtn = findViewById(R.id.likeBtn)
        titleTextView = findViewById(R.id.view_post_title)
        descTextView = findViewById(R.id.view_post_desc)
        viewPosType = findViewById(R.id.view_post_type)



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
        fetchData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.create_post_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.toolbar_edit -> {
                // Handle Edit button click
                val intent = Intent(this, EditPost::class.java)
                intent.putExtra("documentId", documentId)
                startActivity(intent)

                true
            }
            R.id.toolbar_delete -> {
                // Handle Delete button click
                val docRef = db.collection("posts").document(documentId!!)
                docRef
                    .delete()
                    .addOnSuccessListener {
                        Log.d(TAG, "Post successfully deleted!")

                        val inflater: LayoutInflater = layoutInflater
                        val layout: View = inflater.inflate(R.layout.toast, findViewById(R.id.custom_toast_container))

                        val text: TextView = layout.findViewById(R.id.toast_text)
                        text.text = "Post Deleted Successfully"
                        val toast = Toast(applicationContext)
                        toast.duration = Toast.LENGTH_SHORT
                        toast.view = layout
                        toast.show()

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener { e -> Log.w(TAG, "Error deleting Post", e) }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun fetchData(){

        val docRef = db.collection("posts").document(documentId!!)

        docRef.get()
            .addOnSuccessListener { result ->
                if (result.exists()){
                    val title = result.getString("title")
                    val desc = result.getString("description")
                    val type = result.getString("type")


                    titleTextView.text = title
                    descTextView.text = desc
                    viewPosType.text = "Post Type: " + type

                    Log.d("Tag", "title value" + titleTextView.text)

                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }


}