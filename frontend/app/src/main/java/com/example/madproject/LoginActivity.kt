package com.example.madproject

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.madproject.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

        private  lateinit var binding : ActivityLoginBinding
        private lateinit var firebaseAuth: FirebaseAuth
        private lateinit var googleSignInClient: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        //Google Sign in
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this,gso)

        findViewById<Button>(R.id.Google_log_btn).setOnClickListener {
            sigInWithGoogle()
        }



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

    private fun sigInWithGoogle(){

        val signinIntent = googleSignInClient.signInIntent
        launcher.launch(signinIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){

            result ->
        if (result.resultCode == RESULT_OK){

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

        if (firebaseAuth.currentUser != null) {
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }
    }
}