package com.example.madproject.posts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.example.madproject.R

class EditPost : AppCompatActivity() {

    private lateinit var cancelBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_post)

        cancelBtn = findViewById(R.id.cancel_button)

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

    }
}