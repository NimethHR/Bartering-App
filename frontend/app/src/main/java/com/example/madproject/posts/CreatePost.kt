package com.example.madproject.posts

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.madproject.R
import com.example.madproject.models.Post
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage


class CreatePost : AppCompatActivity() {

    private lateinit var createPostTitle: TextInputEditText
    private lateinit var createPostDesc: TextInputEditText
    private lateinit var createPostType: RadioButton
    private lateinit var createPostQuantity: EditText
    private lateinit var uploadBtn: Button
    private lateinit var cancelBtn: Button
    private lateinit var browseBtn: Button
    private lateinit var radioGroup: RadioGroup

    var likeCount: Int = 0
    var documentId: String? = null
    private var fileIntent = Intent()
    private var selectedImageUri: Uri? = null
    private var imageDownloadUrl:String = ""


//    var selectedType: String = ""


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
            val inflater: LayoutInflater = layoutInflater
            val layout: View = inflater.inflate(R.layout.toast, findViewById(R.id.custom_toast_container))

            val text: TextView = layout.findViewById(R.id.toast_text)
            text.text = "Only .png .jpg  .webp images are supported"
            val toast = Toast(applicationContext)
            toast.duration = Toast.LENGTH_LONG
            toast.view = layout
            toast.show()

            fileIntent = Intent()
                .setType("image/*")
                .setAction(Intent.ACTION_GET_CONTENT)

            startActivityForResult(Intent.createChooser(fileIntent, "Select a file"), 111)
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 111 && resultCode == RESULT_OK) {
            selectedImageUri = data?.data // The URI with the location of the file

//            if (imageUri != null) {
//                // Handle the image URI (e.g., upload it to Firebase Storage)
//                if (documentId != null){
//                    uploadImage(imageUri)
//                }
//            }
        }
    }

    private fun uploadImage(imageUri: Uri){
        val storage = Firebase.storage
        var storageRef = storage.reference
        var imageRef: StorageReference? = storageRef.child("posts/$documentId.jpg")

//        Log.d("Tag", "selected radio button: $imageUri")

        val uploadTask = imageRef?.putFile(imageUri)
        uploadTask?.addOnSuccessListener {
            val imageUrlTask = imageRef?.downloadUrl // Image uploaded successfully
            imageUrlTask?.addOnSuccessListener { uri ->

                imageDownloadUrl = uri.toString()
            }
        }?.addOnFailureListener { exception ->
            // Handle any errors that occurred during the upload
            Log.e(TAG, "Error uploading image: ${exception.message}")
        }
    }
    private fun uploadData(){
        val db = Firebase.firestore


        val title = createPostTitle.text.toString()
        val desc = createPostDesc.text.toString()
        val number: Int = createPostQuantity.text.toString().toInt()

        if (title.isEmpty() || desc.isEmpty()){
            createPostTitle.error = "Please enter a value"
        }


        radioGroup = findViewById(R.id.type_radio_group)

        val selectedRadioButtonId = radioGroup.checkedRadioButtonId
        val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)
        val selectedType = selectedRadioButton.text.toString()


        val post = Post(title, desc, selectedType , number, likeCount)

        db.collection("posts")
            .add(post)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                documentId = documentReference.id

                Log.d("Tag", "document id in CreatePost: $documentId")

                if (selectedImageUri != null){
                    uploadImage(selectedImageUri!!)
                }

                val inflater: LayoutInflater = layoutInflater
                val layout: View = inflater.inflate(R.layout.toast, findViewById(R.id.custom_toast_container))

                val text: TextView = layout.findViewById(R.id.toast_text)
                text.text = "Post Uploaded Successfully"
                val toast = Toast(applicationContext)
                toast.duration = Toast.LENGTH_SHORT
                toast.view = layout
                toast.show()

                val intent = Intent(this, ViewPost::class.java)
                intent.putExtra("documentId", documentId)
                intent.putExtra("imageDownloadUrl", imageDownloadUrl)
                startActivity(intent)
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }


    }
}