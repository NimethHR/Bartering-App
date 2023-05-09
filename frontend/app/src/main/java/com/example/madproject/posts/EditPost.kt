package com.example.madproject.posts

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.madproject.R
import com.example.madproject.models.Post
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class EditPost : AppCompatActivity() {

    private lateinit var editBtn: Button
    private lateinit var cancelBtn: Button
    private lateinit var editFileBtn: Button
    private lateinit var editDesc: TextInputEditText
    private lateinit var radioButtonHave: RadioButton
    private lateinit var radioButtonNeed: RadioButton
    private lateinit var radioButtonFree: RadioButton
    private lateinit var editQuantity: EditText
    private lateinit var radioGroup: RadioGroup

    private var editTitle: String? = ""
    private var likeCount: Int? = 0
    private var fileIntent = Intent()
    private var selectedImageUri: Uri? = null
    private var imageDownloadUrl:String = ""






    private  var documentId: String? = null

    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_post)

        documentId = intent.getStringExtra("documentId").toString()

        editBtn = findViewById(R.id.edit_button)
        cancelBtn = findViewById(R.id.cancel_button)
        editDesc = findViewById(R.id.edit_post_desc)
        editQuantity = findViewById(R.id.editTextNumberDecimal2)
        radioButtonHave = findViewById(R.id.haveRadioButton)
        radioButtonNeed = findViewById(R.id.needRadioButton)
        radioButtonFree = findViewById(R.id.freeRadioButton)
        editFileBtn = findViewById(R.id.file_edit_btn)


        // TODO: fix radio button
//        viewPosType = findViewById(R.id.view_post_type)

        fetchData()

        cancelBtn.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Discard Edit?")
            alertDialogBuilder.setMessage("All changes will be discarded")

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

        editBtn.setOnClickListener {
            uploadData()
        }

        editFileBtn.setOnClickListener {
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
            Log.e(ContentValues.TAG, "Error uploading image: ${exception.message}")
        }
    }

    private fun fetchData(){

        val docRef = db.collection("posts").document(documentId!!)
        docRef.get()
            .addOnSuccessListener { result ->
                if (result.exists()){
                    Log.d("Tag", "Data fetched successfully in edit post")

                    val post = result.toObject(Post::class.java)

//                    val desc = result.getString("description")
//                    val type = result.getString("type")
//                    val quantity = result.getString("type")
                    editTitle = post?.title
                    likeCount = post?.likes
                    editDesc.setText(post?.description)
                    when (post?.type) {
                        "Have" -> radioButtonHave.isChecked = true
                        "Need" -> radioButtonNeed.isChecked = true
                        "Free" -> radioButtonFree.isChecked = true
                    }
                    editQuantity.setText(post?.quantity.toString())
                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
    }

    private fun uploadData(){
        val desc = editDesc.text.toString()
        val number: Int = editQuantity.text.toString().toInt()

        radioGroup = findViewById(R.id.edit_radio_group)

        val selectedRadioButtonId = radioGroup.checkedRadioButtonId
        val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)
        val selectedType = selectedRadioButton.text.toString()

        val post = Post(editTitle, desc, selectedType , number, likeCount)

        db.collection("posts").document(documentId!!)
            .set(post)
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "Document edited with ID: $documentId")

                Log.d("Tag", "selected radio button: $selectedType")

                if (selectedImageUri != null){
                    uploadImage(selectedImageUri!!)
                }


                val inflater: LayoutInflater = layoutInflater
                val layout: View = inflater.inflate(R.layout.toast, findViewById(R.id.custom_toast_container))

                val text: TextView = layout.findViewById(R.id.toast_text)
                text.text = "Post Edited Successfully"
                val toast = Toast(applicationContext)
                toast.duration = Toast.LENGTH_SHORT
                toast.view = layout
                toast.show()

                val intent = Intent(this, ViewPost::class.java)
                intent.putExtra("documentId", documentId)
                startActivity(intent)
            }

    }
}