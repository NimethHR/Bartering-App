package com.example.userui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.userui.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//initialize the binding , pass layoutinflator
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initialize
        firebaseAuth = FirebaseAuth.getInstance()

        binding.siginnav.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.regBtn.setOnClickListener {

            val email = binding.regEmail.text.toString()
            val pass = binding.regPwd1.text.toString()
            val confirmPass = binding.regPwd2.text.toString()


            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {

                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, AddprofActivity::class.java)
                            startActivity(intent)
                            Toast.makeText(this, "Successfully Registered", Toast.LENGTH_SHORT).show()

                        } else {
                            Toast.makeText(this, "Reg Failed", Toast.LENGTH_SHORT).show()
                        }
                    }

                } else {
                    Toast.makeText(this, "Passwords Are Not Matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Fill All The Fields ", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        if(firebaseAuth.currentUser != null){
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }

    }
}