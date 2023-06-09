package com.example.madproject.fragments

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
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
 * Use the [AddFeedback.newInstance] factory method to
 * create an instance of this fragment.
 */

class AddFeedback : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var regex:Regex? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_feedback, container, false)

        val submitBtn = view.findViewById<Button>(R.id.addFeedBtn)
        val db = Firebase.firestore

        submitBtn.setOnClickListener {

        val feedback = view.findViewById<TextView>(R.id.feedText).text.toString()

//            validate the feedback
            if(feedback.length == 0){
                Toast.makeText(activity,"Feedback is empty", Toast.LENGTH_SHORT).show()
            }else if(badwordDetector(feedback)== true){
                Toast.makeText(activity,"Do not enter bad words", Toast.LENGTH_SHORT).show()
        }else{
            val feed = hashMapOf(
            "Feedback" to feedback
        )
            db.collection("Feedbacks")
                .add(feed)
                .addOnSuccessListener { docReference ->
                    Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${docReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error adding document", e)
                }
//            goto home is here
            Toast.makeText(activity,"Feedback is added", Toast.LENGTH_SHORT).show()
            parentFragmentManager.beginTransaction().replace(R.id.fragment_container,HomePage()).addToBackStack(null).commit()
                }
            }
        return view
    }

    private fun badwordDetector(word:String):Boolean{
        regex = Regex(pattern = "(badword1|badword2)")
        val matched = regex!!.containsMatchIn(input = word)
        return matched
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddFeedback.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddFeedback().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}