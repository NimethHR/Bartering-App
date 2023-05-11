package com.example.madproject

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.madproject.databinding.ActivityRegisterBinding
import com.example.madproject.fragments.HomePage
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//initialize the binding , pass layoutinflator
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initialize auth
        firebaseAuth = FirebaseAuth.getInstance()

        //Google Sign in
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient =GoogleSignIn.getClient(this,gso)

        findViewById<Button>(R.id.Google_reg_btn).setOnClickListener {
            sigInWithGoogle()
        }



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

    private fun sigInWithGoogle(){

        val signinIntent = googleSignInClient.signInIntent
        launcher.launch(signinIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){

        result ->
            if (result.resultCode == Activity.RESULT_OK){

                val  task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                results(task)

            }
    }

    private fun results(task : Task<GoogleSignInAccount>){

        if (task.isSuccessful){
           val current : GoogleSignInAccount? = task.result
            if (current!= null){
                navigateto(current)
            }

        }else{
            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun navigateto(current: GoogleSignInAccount) {
        val logged = GoogleAuthProvider.getCredential(current.idToken,null)
        firebaseAuth.signInWithCredential(logged).addOnCompleteListener{
            if (it.isSuccessful){
                val intent = Intent(this, AddprofActivity::class.java)
                startActivity(intent)
                Toast.makeText(this, "Successfully Logged in With Google", Toast.LENGTH_SHORT).show()

            }else{

                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show()
                Toast.makeText(this, "Google Log in Failed", Toast.LENGTH_SHORT).show()

            }
        }

    }


    override fun onStart() {
        super.onStart()

        if(firebaseAuth.currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}