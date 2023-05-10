package com.example.madproject


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserProfileActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var displayname: TextView
    private lateinit var displayemail: TextView
    private lateinit var displaypnumb: TextView
    private lateinit var displaytellnumb: TextView
    private lateinit var displayAddress: TextView
    private lateinit var displaydesc: TextView

    private lateinit var editButton: Button
    private lateinit var logoutButton: Button
    private lateinit var deleteuserbtn :Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        //database reference
        val db = Firebase.firestore
        //initialize
        firebaseAuth = FirebaseAuth.getInstance()
        //getting current logged in users uid and turning it in a to string
        val uid = firebaseAuth.currentUser?.uid
        val suid = uid.toString()


        displayname = findViewById(R.id.profile_username)
        displayemail = findViewById(R.id.profile_email)
        displaypnumb = findViewById(R.id.pmobile_num)
        displaytellnumb = findViewById(R.id.ptell_num)
        displayAddress = findViewById(R.id.paddress_num)
        displaydesc = findViewById(R.id.p_description)

        editButton =findViewById(R.id.peditbutton)
        logoutButton =findViewById(R.id.logout)
        deleteuserbtn =findViewById(R.id.pdeleteuserbtn)


        //!! return non null value
        //getting a specific document with current users uid
        val docRef = db.collection("users").document(suid)
        docRef.get()
            .addOnSuccessListener { document ->
                //checks if document is null or not
                if (document != null) {

                    val rname = document.data!!["displayName"].toString()
                    val remail = document.data!!["email"].toString()
                    val rpnumb = document.data!!["Phone Number"].toString()
                    val rtellnumb = document.data!!["Tell Number"].toString()
                    val raddres = document.data!!["Address"].toString()
                    val rdesc = document.data!!["Description"].toString()

                    displayname.text = rname
                    displayemail.text = remail
                    displaypnumb.text = rpnumb
                    displaytellnumb.text = rtellnumb
                    displayAddress.text = raddres
                    displaydesc.text = rdesc


                }else{
                    Toast.makeText(this, "The Document is Empty ", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to Display Data", Toast.LENGTH_LONG).show()
            }


    editButton.setOnClickListener {
        val intent = Intent(this,  UpdateprofActivity::class.java)
        startActivity(intent)
    }
        //by clicking logout button current user will be logged out and navigated to login activity
     logoutButton.setOnClickListener {

         firebaseAuth.signOut()

         val intent = Intent(this,  LoginActivity::class.java)
         startActivity(intent)
     }
        //navigated to delete user activity
        deleteuserbtn.setOnClickListener {
            val intent = Intent(this,  DeleteUserActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onStart() {
        super.onStart()

        if(firebaseAuth.currentUser == null){
            val intent = Intent(this,  LoginActivity::class.java)
            startActivity(intent)
        }

    }

}

















