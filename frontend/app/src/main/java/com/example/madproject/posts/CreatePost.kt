package com.example.madproject.posts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.databinding.DataBindingUtil
import com.example.madproject.R
import com.example.madproject.databinding.ActivityCreatePostBinding

class CreatePost : AppCompatActivity() {

    private lateinit var binding: ActivityCreatePostBinding

    private lateinit var create_post_title: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_post)

        create_post_title = findViewById(R.id.create_post_title)

    }
}