package com.example.madproject

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ProfileDisplayFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var displayname: TextView
    private lateinit var displayemail: TextView
    private lateinit var displaypnumb: TextView
    private lateinit var displaytellnumb: TextView
    private lateinit var displayAddress: TextView
    private lateinit var displaydesc: TextView

    private lateinit var editButton: Button
    private lateinit var logoutButton: Button
    private lateinit var deleteuserbtn : Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_display, container, false)

        //database reference
        val db = Firebase.firestore
        //initialize
        firebaseAuth = FirebaseAuth.getInstance()
        //getting current logged in users uid and turning it in a to string
        val uid = firebaseAuth.currentUser?.uid
        val suid = uid.toString()


        displayname = view.findViewById(R.id.dprofile_username)
        displayemail = view.findViewById(R.id.dprofile_email)
        displaypnumb = view.findViewById(R.id.dpmobile_num)
        displaytellnumb = view.findViewById(R.id.dptell_num)
        displayAddress = view.findViewById(R.id.dpaddress_num)
        displaydesc = view.findViewById(R.id.dp_description)

        editButton =view.findViewById(R.id.dpeditbutton)
        logoutButton =view.findViewById(R.id.dlogout)
        deleteuserbtn =view.findViewById(R.id.dpdeleteuserbtn)


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
                    Toast.makeText(requireContext(), "The Document is Empty ", Toast.LENGTH_SHORT).show()

                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to Display Data", Toast.LENGTH_LONG).show()
            }


        editButton.setOnClickListener {
            val intent = Intent(requireContext(),  UpdateprofActivity::class.java)
            startActivity(intent)
        }
        //by clicking logout button current user will be logged out and navigated to login activity
        logoutButton.setOnClickListener {

            firebaseAuth.signOut()

            val intent = Intent(requireContext(),  LoginActivity::class.java)
            startActivity(intent)
        }
        //navigated to delete user activity
        deleteuserbtn.setOnClickListener {
            val intent = Intent(requireContext(),  DeleteUserActivity::class.java)
            startActivity(intent)
        }


        // Inflate the layout for this fragment
        return view
    }


}