package com.example.madproject

import android.content.ContentValues

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.madproject.posts.ViewPost
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth

import android.util.Log
import android.widget.Button

import com.example.madproject.fragments.GroupListFragment
import com.example.madproject.fragments.AdminDashboard
import com.example.madproject.fragments.CreatePostFragment
import com.example.madproject.fragments.GroupListFragment
import com.example.madproject.fragments.HomePage

import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottom_nav_bar)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_nav_bar_groups ->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, GroupListFragment()).commit()
                    true
                }
                R.id.bottom_nav_bar_profile -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ProfileDisplayFragment()).commit()
                    true
                }
                R.id.bottom_nav_bar_dashboard -> {
//                    Navbar button to admin_dash board
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, AdminDashboard()).commit()
                    true
                }
                R.id.bottom_nav_bar_post -> {
                   supportFragmentManager.beginTransaction().replace(R.id.fragment_container, CreatePostFragment()).commit()
                  true
                }
                R.id.bottom_nav_bar_home -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomePage()).commit()
                    true
                }
                else -> false
            }
        }

    }

    override fun onStart() {
        super.onStart()

        val auth = Firebase.auth

        auth.signOut()
        auth.signInWithEmailAndPassword("test@gmail.com", "password")
    }

    }
