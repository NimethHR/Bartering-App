package com.example.madproject.posts

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.madproject.MainActivity
import com.example.madproject.R
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class ViewPost : AppCompatActivity() {

    private  var documentId: String? = null

    private lateinit var likeBtn: Button
    private lateinit var titleTextView: MaterialTextView
    private lateinit var descTextView: MaterialTextView
    private lateinit var viewPosType: MaterialTextView
    private lateinit var viewPostQuantity: MaterialTextView
    private lateinit var postImage: ImageView


    var likeCount: Int = 0
    var isLiked = false
    var quantity: Int = 0

    private val db = Firebase.firestore
    private val storage = Firebase.storage


//    private lateinit var imageDownloadUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_post)

        documentId = intent.getStringExtra("documentId").toString()
//        imageDownloadUrl = intent.getStringExtra("imageDownloadUrl").toString()

        Log.d("Tag", "document id in ViewPost: $documentId")
//        Log.d("Tag", "image download uri: $imageDownloadUrl")


        val toolbar = findViewById<Toolbar>(R.id.create_toolbar)
        setSupportActionBar(toolbar)

        likeBtn = findViewById(R.id.likeBtn)
        titleTextView = findViewById(R.id.view_post_title)
        descTextView = findViewById(R.id.view_post_desc)
        viewPosType = findViewById(R.id.view_post_type)
        viewPostQuantity = findViewById(R.id.view_post_quantity)
        postImage = findViewById(R.id.imageView3)

        likeBtn.setOnClickListener {
            if (isLiked){
                isLiked = false
                likeCount--
                if (likeCount!=0){
                    likeBtn.text = "Liked " + likeCount
                }
                else{
                    likeBtn.text = "Like"
                }
            }
            else{
                likeCount++
                isLiked = true
                likeBtn.text = "Liked $likeCount"
            }
            likeUpdate()
        }
        fetchData()
    }

    private fun likeUpdate(){
        val likes = hashMapOf(
            "likes" to likeCount
        )

        db.collection("posts").document(documentId!!)
            .update("likes", likeCount)
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "Like count updated for Likes = $likeCount")
            }
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


                val alertDialogBuilder = AlertDialog.Builder(this)
                alertDialogBuilder.setTitle("Confirm Delete")
                alertDialogBuilder.setMessage("Are you sure you want to delete this post?")

                //TODO - Add +ve and -ve options

                alertDialogBuilder.setPositiveButton("Delete") { dialog, which ->
                    // Perform the delete operation here
                    // Example: deleteItem()

                    // Handle Delete button click
                    val docRef = db.collection("posts").document(documentId!!)
                    docRef
                        .delete()
                        .addOnSuccessListener {
                            Log.d(TAG, "Post successfully deleted from firestore")


                            val storage = Firebase.storage
                            var storageRef = storage.reference
                            var imageRef: StorageReference? = storageRef.child("posts/$documentId.jpg")

                            imageRef?.delete()
                                ?.addOnSuccessListener {
                                    Log.d(TAG, "Image file successfully deleted from cloud storage")

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

                                }?.addOnFailureListener {
                                    Log.d(TAG, "Error deleting Image file from cloud storage")
                                }
                        }
                        .addOnFailureListener { e -> Log.w(TAG, "Error deleting Post", e) }

                }
                alertDialogBuilder.setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }

                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun fetchData(){

        var storageRef = storage.reference
        var imageRef = storageRef.child("posts/$documentId.jpg")

        val ONE_MEGABYTE: Long = 1024 * 1024
        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
            // Data for "images/island.jpg" is returned, use this as needed
            Log.w(TAG, "File downloaded from storage")

        }.addOnFailureListener {
            // Handle any errors
        }

        val docRef = db.collection("posts").document(documentId!!)

        docRef.get()
            .addOnSuccessListener { result ->
                if (result.exists()){
                    val title = result.getString("title")
                    val desc = result.getString("description")
                    val type = result.getString("type")
                    val longLikeCount = result.getLong("likes")
                    val longQuantity = result.getLong("quantity")

                    quantity =longQuantity?.toInt()?: 0

                    likeCount = longLikeCount?.toInt()?: 0

                    titleTextView.text = title
                    descTextView.text = desc
                    viewPostQuantity.text = "Amount of units: " + quantity.toString()
                    viewPosType.text = "Post Type: " + type


                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }


}