package com.example.madproject.posts

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.madproject.R
import com.example.madproject.models.Post
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class CreatePost : AppCompatActivity() {

    private lateinit var createPostTitle: TextInputEditText
    private lateinit var createPostDesc: TextInputEditText
    private lateinit var createPostType: RadioButton
    private lateinit var createPostQuantity: EditText
    private lateinit var uploadBtn: Button
    private lateinit var cancelBtn: Button
    private lateinit var browseBtn: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

//        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_post)

        createPostTitle = findViewById(R.id.create_post_title_input)
        createPostDesc = findViewById(R.id.create_post_desc_input)
        createPostType = findViewById(R.id.haveRadioButton)
        createPostQuantity = findViewById(R.id.editTextNumberDecimal2)
        uploadBtn = findViewById(R.id.upload_post_btn)
        cancelBtn = findViewById(R.id.cancel_button)
        browseBtn = findViewById(R.id.file_browse_btn)



        uploadBtn.setOnClickListener {
            uploadData()
        }

        cancelBtn.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Discard Post?")
            alertDialogBuilder.setMessage("Data will be discarded. You cannot undo this process")

            //TODO - Add +ve and -ve options

            alertDialogBuilder.setPositiveButton("Discard") { dialog, which ->
                // Perform the delete operation here
                // Example: deleteItem()
            }
            alertDialogBuilder.setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }

        browseBtn.setOnClickListener {
//            val message = "Only .png .jpg .webp images are supported"
//            val duration = Toast.LENGTH_LONG
//            val toast = Toast.makeText(this, message, duration)
//            toast.show()

//            val inflater: LayoutInflater = layoutInflater
//            val layout: View = inflater.inflate(R.layout.toast, findViewById(R.id.custom_toast_container))
//
//            val text: TextView = layout.findViewById(R.id.toast_text)
//            text.text = "Only .png .jpg  .webp images are supported"
//
//            val toast = Toast(applicationContext)
//            toast.duration = Toast.LENGTH_LONG
//            toast.view = layout
//            toast.show()

            val inflater: LayoutInflater = layoutInflater
            val layout: View = inflater.inflate(R.layout.toast, findViewById(R.id.custom_toast_container))

            val text: TextView = layout.findViewById(R.id.toast_text)
            text.text = "Only .png .jpg  .webp images are supported"
            val toast = Toast(applicationContext)
            toast.duration = Toast.LENGTH_LONG
            toast.view = layout
            toast.show()

        }
    }
    private fun uploadData(){
        val db = Firebase.firestore

        var documentId: String? = null
        val title = createPostTitle.text.toString()
        val desc = createPostDesc.text.toString()
        val number: Int = createPostQuantity.text.toString().toInt()

        if (title.isEmpty() || desc.isEmpty()){
            createPostTitle.error = "Please enter a value"
        }

        val post = Post(title, desc,null , number)

        db.collection("posts")
            .add(post)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                documentId = documentReference.id
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
        val intent = Intent(this, ViewPost::class.java)
        intent.putExtra("documentId", documentId)
        startActivity(intent)
    }
}