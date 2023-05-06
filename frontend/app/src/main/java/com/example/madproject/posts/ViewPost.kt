package com.example.madproject.posts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.example.madproject.R

class ViewPost : AppCompatActivity() {

    private lateinit var likeBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_post)

        likeBtn = findViewById(R.id.likeBtn)

        likeBtn.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Confirm Delete")
            alertDialogBuilder.setMessage("Are you sure you want to delete this post?")

            //TODO - Add +ve and -ve options

            alertDialogBuilder.setPositiveButton("Delete") { dialog, which ->
                // Perform the delete operation here
                // Example: deleteItem()
            }
            alertDialogBuilder.setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }

    }
}