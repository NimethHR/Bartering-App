package com.example.madproject.posts

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.example.madproject.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.example.madproject.models.Post

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
            val message = "Only .png .jpg  .webp images are supported"
            val duration = Toast.LENGTH_LONG
            val toast = Toast.makeText(this, message, duration)
            toast.show()

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
        }
    }
    private fun uploadData(){
        val db = Firebase.firestore


        val title = createPostTitle.text.toString()
        val desc = createPostDesc.text.toString()
//        val quantity = createPostQuantity
        val number: Int = createPostQuantity.text.toString().toInt()

        if (title.isEmpty() || desc.isEmpty()){
            createPostTitle.error = "Please enter a value"
        }

//        val post = hashMapOf(
//            "title" to title,
//            "description" to desc,
//            "quantity" to number
//        )

        val post = Post(title, desc,null , number)

        db.collection("posts")
            .add(post)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
        val intent = Intent(this, ViewPost::class.java)
        startActivity(intent)
    }
}