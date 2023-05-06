package com.example.userui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UpdateprofActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var upname :EditText
    private lateinit var upemail :EditText
    private lateinit var upnumb :EditText
    private lateinit var uptellnumb :EditText
    private lateinit var updesc :EditText
    private lateinit var upaddres :EditText
    private lateinit var upbtn :Button


    //database reference
    private val db = Firebase.firestore



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_updateprof)

        //initialize
        firebaseAuth = FirebaseAuth.getInstance()
        //getting current logged in users uid and turning it in a to string
        val uid = firebaseAuth.currentUser?.uid
        val suid = uid.toString()

        upname =findViewById(R.id.uprofusername)
        upemail=findViewById(R.id.uprofemail)
        upnumb=findViewById(R.id.uprofphnum)
        uptellnumb=findViewById(R.id.uproftellnumb)
        updesc=findViewById(R.id.uprofaddress)
        upaddres=findViewById(R.id.uprofdesc)
        upbtn =findViewById(R.id.updateDatabtn)

        updateData()

        upbtn.setOnClickListener {

            val sUserName = upname.text.toString()
            val sConEmail =upemail.text.toString()
            val spnumb =upnumb.text.toString()
            val stellnumb = uptellnumb.text.toString()
            val spaddress = upaddres.text.toString()
            val spdesc = updesc.text.toString()


            // checking if the2 fields are empty
            if(sUserName.isNotEmpty() && sConEmail.isNotEmpty() && spnumb.isNotEmpty()
                && stellnumb.isNotEmpty() && spaddress.isNotEmpty() && spdesc.isNotEmpty()){

                val user = hashMapOf(
                    "displayName" to sUserName,
                    "email" to sConEmail,
                    "Phone Number" to spnumb,
                    "Tell Number" to stellnumb,
                    "Address" to spaddress,
                    "Description" to spdesc
                )
                //storing in a document that has current users uid as the id
                //also notify success or failure
                db.collection("users").document(suid)
                    .set(user)
                    .addOnSuccessListener {
                        val intent = Intent(this,  UserProfileActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(this, "Data Updated Successfully", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener{
                        Toast.makeText(this, "Data Updating Failed", Toast.LENGTH_SHORT).show()
                    }


            }else{
                Toast.makeText(this, "Fill All The Fields ", Toast.LENGTH_SHORT).show()
            }

        }


    }

    private fun updateData(){


        //initialize
        firebaseAuth = FirebaseAuth.getInstance()
        val uid = firebaseAuth.currentUser?.uid
        val suid = uid.toString()


        //document reference
        val docref = db.collection("users").document(suid)

        docref.get()
            .addOnSuccessListener {
                //it <-document snapshot
                //checking if document is null or not
                if (it != null){
                    val updateName = it.data!!["displayName"].toString()
                    val updateEmail = it.data!!["email"].toString()
                    val updatePnumb = it.data!!["Phone Number"].toString()
                    val updateTellnumb = it.data!!["Tell Number"].toString()
                    val updateAddres = it.data!!["Address"].toString()
                    val updateDesc = it.data!!["Description"].toString()

                    upname.setText(updateName)
                    upemail.setText(updateEmail)
                    upnumb.setText(updatePnumb)
                    uptellnumb.setText(updateTellnumb)
                    upaddres.setText(updateAddres)
                    updesc.setText(updateDesc)

                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Something went Wrong  ", Toast.LENGTH_LONG).show()
            }


    }
}








