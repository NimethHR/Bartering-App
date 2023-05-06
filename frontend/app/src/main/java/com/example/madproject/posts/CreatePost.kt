package com.example.madproject.posts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import androidx.databinding.DataBindingUtil
import com.example.madproject.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CreatePost : AppCompatActivity() {

    private lateinit var createPostTitle: TextInputEditText
    private lateinit var createPostDesc: TextInputEditText
    private lateinit var createPostType: RadioButton
    private lateinit var createPostQuantity: EditText
    private lateinit var uploadBtn: Button
    private lateinit var cancelBtn: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

//        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_post)

        val db = Firebase.firestore

        createPostTitle = findViewById(R.id.create_post_title_input)
        createPostDesc = findViewById(R.id.create_post_desc_input)
        createPostType = findViewById(R.id.haveRadioButton)
        createPostQuantity = findViewById(R.id.editTextNumberDecimal2)
        uploadBtn = findViewById(R.id.upload_post_btn)
        cancelBtn = findViewById(R.id.cancel_button)


        uploadBtn.setOnClickListener {

        }
    }
    private fun uploadData(){
        val title = createPostTitle.text.toString()
        val desc = createPostDesc.text.toString()
//        val quantity = createPostQuantity
        val number: Int = createPostQuantity.text.toString().toInt()

        if (title.isEmpty() || desc.isEmpty()){
            createPostTitle.error = "Please enter a value"
        }
    }
}