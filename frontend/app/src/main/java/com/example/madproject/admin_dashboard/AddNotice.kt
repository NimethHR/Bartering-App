package com.example.test.admin_dashboard

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import com.example.test.R
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class AddNotice : AppCompatActivity() {

//    setup database and storage
    private val db = Firebase.firestore
    private val storage = FirebaseStorage.getInstance()

//    create reference
    private var storageRef = storage.reference

    private val pickImage = 10
    private var imageUri: Uri? = null
    private lateinit var image:ImageView
    private var imageRef:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notice)

        image = findViewById(R.id.image)
        val title = findViewById<EditText>(R.id.title_btn).text
        val description = findViewById<EditText>(R.id.description_btn).text
        val imagebtn = findViewById<Button>(R.id.add_image_btn)
        val submit = findViewById<Button>(R.id.submit_btn)


        imagebtn.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
            Toast.makeText(this,intent.toString(),Toast.LENGTH_SHORT).show()
        }

        submit.setOnClickListener {

        val notice = hashMapOf(
            "title" to title.toString(),
            "description" to description.toString()
        )

//            enter the data to database
        db.collection("Notices")
            .add(notice)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }

//            enter the image to storage
//        val imageToDB = storageRef.child("notices/$imageRef")
//            imageUri?.let { it1 ->
//                imageToDB.putFile(it1)
//                    .addOnSuccessListener {
//                        Toast.makeText(this,"Image is added",Toast.LENGTH_SHORT).show()
//                    }
//                    .addOnFailureListener {
//                        Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show()
//                    }
//            }


//            Toast.makeText(this,imageRef,Toast.LENGTH_SHORT).show()
        Toast.makeText(this,"Notice is added", Toast.LENGTH_SHORT).show()
        val intent = Intent(this,Dashboard::class.java)
        startActivity(intent)
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            imageRef = imageUri.toString()
            image.setImageURI(imageUri)
        }
    }

}
