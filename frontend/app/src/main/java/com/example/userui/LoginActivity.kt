package com.example.userui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.userui.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

        private  lateinit var binding: ActivityLoginBinding
        private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.regnav.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.loginbtn.setOnClickListener{

            val email = binding.loginemail.text.toString()
            val pass = binding.loginpwd.text.toString()


//checking if fields are empty or not if empty error is shown
            if (email.isNotEmpty() && pass.isNotEmpty()) {

                    firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, UserProfileActivity::class.java)
                            startActivity(intent)
                            Toast.makeText(this, "Successfully Logged in", Toast.LENGTH_SHORT)
                                .show()

                        } else {
                            Toast.makeText(this, "Log in Failed", Toast.LENGTH_SHORT).show()
                        }
                    }

            } else {
                Toast.makeText(this, "Provide All The Credentials ", Toast.LENGTH_SHORT).show()
            }


        }
    }

    override fun onStart() {
        super.onStart()

        if (firebaseAuth.currentUser != null) {
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }
    }
}