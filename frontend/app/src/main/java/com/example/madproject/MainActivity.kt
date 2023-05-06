package com.example.madproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.madproject.posts.ViewPost
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView

class MainActivity : AppCompatActivity() {

    private lateinit var card1: MaterialTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        card1 = findViewById(R.id.view_post_title1)

        card1.setOnClickListener{
            val intent = Intent(this, ViewPost::class.java)
            startActivity(intent)
        }
    }

}