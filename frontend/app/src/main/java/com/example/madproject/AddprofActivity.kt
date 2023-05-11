package com.example.madproject


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
//import com.example.userui.databinding.ActivityRegisterBinding
//import com.example.userui.databinding.ActivitySavepdataBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddprofActivity : AppCompatActivity() {

//declaring
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var  userName : EditText
    private lateinit var  conemail : EditText
    private lateinit var  pnumb: EditText
    private lateinit var  tellnumb : EditText
    private lateinit var  paddress : EditText
    private lateinit var  pdesc : EditText
    private lateinit var  addbtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_savepdata)

        val db = Firebase.firestore

        //setting values by the id
        userName=findViewById(R.id.profusername)
        conemail=findViewById(R.id.profemail)
        pnumb=findViewById(R.id.profphnum)
        tellnumb=findViewById(R.id.proftellnumb)
        paddress=findViewById(R.id.profaddress)
        pdesc=findViewById(R.id.profdesc)
        addbtn=findViewById(R.id.addDatabtn)

        //initialize
        firebaseAuth = FirebaseAuth.getInstance()
        //getting current logged in users uid and turning it in a to string
        val uid = firebaseAuth.currentUser?.uid
        val suid = uid.toString()

        //will start on button click
        addbtn.setOnClickListener {

            val sUserName = userName.text.toString()
            val sConEmail =conemail.text.toString()
            val spnumb =pnumb.text.toString()
            val stellnumb = tellnumb.text.toString()
            val spaddress = paddress.text.toString()
            val spdesc = pdesc.text.toString()


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
                        val intent = Intent(this,  MainActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(this, "Data Added Successfully", Toast.LENGTH_SHORT).show()

                        val user = Firebase.auth.currentUser

                        val profileUpdates = userProfileChangeRequest {
                            displayName = sUserName
                        }

                        user!!.updateProfile(profileUpdates)

                    }
                    .addOnFailureListener{
                        Toast.makeText(this, "Data Adding Failed", Toast.LENGTH_SHORT).show()
                    }


            }else{
                Toast.makeText(this, "Fill All The Fields ", Toast.LENGTH_SHORT).show()
            }
        }

    }

}
















