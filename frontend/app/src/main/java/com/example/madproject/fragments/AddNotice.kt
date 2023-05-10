package com.example.madproject.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.madproject.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddNotice.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddNotice : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

//    ##############################################
    private lateinit var view : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var add_button: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val db = Firebase.firestore
        view = inflater.inflate(R.layout.fragment_add_notice, container, false)
        add_button = view.findViewById(R.id.addBtn)

        add_button.setOnClickListener {

//            Extract the data from xml
        var title = view.findViewById<EditText>(R.id.title).text.toString()
        var description = view.findViewById<EditText>(R.id.description).text.toString()
//                Validate the input data
            if(title.length == 0 ){
                Toast.makeText(activity,"Notice title is empty", Toast.LENGTH_SHORT).show()
            }else if(description.length == 0){
                Toast.makeText(activity,"Notice description is empty", Toast.LENGTH_SHORT).show()
            }else{
//                Enter the data to database
        val notice = hashMapOf(
            "title" to title,
            "description" to description
        )
            db.collection("Notices")
                .add(notice)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }

            Toast.makeText(activity,"Notice is added", Toast.LENGTH_SHORT).show()
            parentFragmentManager.beginTransaction().replace(R.id.fragment_container,AdminDashboard()).addToBackStack(null).commit()
            }
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddNotice.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddNotice().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}