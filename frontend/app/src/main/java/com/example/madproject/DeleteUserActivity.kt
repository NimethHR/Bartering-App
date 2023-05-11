package com.example.madproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class DeleteUserActivity : AppCompatActivity() {


    private lateinit var deletebtn : Button
    private lateinit var gobackbtn : Button

    private lateinit var firebaseAuth: FirebaseAuth
    private var user: FirebaseUser? =null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_user)

        val dbref = FirebaseFirestore.getInstance()

        //initialize
        firebaseAuth = FirebaseAuth.getInstance()
        //getting current logged in user
        val currentuser = firebaseAuth.currentUser
        //getting current logged in users uid and turning it in a to string
        val uid = firebaseAuth.currentUser?.uid
        val suid = uid.toString()

        deletebtn = findViewById(R.id.deleteuserbtn)
        gobackbtn = findViewById(R.id.backtoprofile)

        gobackbtn.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


//deletes document and user onclick
        deletebtn.setOnClickListener {


            val documentPath = "users/${suid}"
// alerts the user that the document is deleted
            dbref.document(documentPath)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "User Document Deleted Successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "User  Document Deletion Unsuccessfull", Toast.LENGTH_SHORT).show()
                }




// alerts user that the account is deleted
            currentuser?.delete()
                ?.addOnCompleteListener {
                    if (it.isSuccessful){
                        val intent = Intent(this,  LoginActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(this, "User Deleted Successfully", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this, "User Deletion Unsuccessfully", Toast.LENGTH_SHORT).show()
                    }
                }

        }
    }
}