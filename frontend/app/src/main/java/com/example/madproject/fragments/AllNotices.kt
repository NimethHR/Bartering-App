package com.example.madproject.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madproject.R
import com.example.madproject.models.Notice
import com.example.madproject.adapters.NoticeAdp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AllNotices.newInstance] factory method to
 * create an instance of this fragment.
 */
class AllNotices : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_all_notices, container, false)
        val db = Firebase.firestore

        val addNotice = view.findViewById<Button>(R.id.addNewNotice)
        val recyclerView = view.findViewById<RecyclerView>(R.id.NoticeList)

        val noticesRef = db.collection("Notices")
        val noticesList = ArrayList<Notice>()

        val adapter = NoticeAdp(noticesList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        noticesRef.get().addOnSuccessListener { documents ->
            for (document in documents) {
                val title = document.getString("title") ?: ""
                val description = document.getString("description") ?: ""
                val notice = Notice(document.id,title, description)
                noticesList.add(notice)
            }
            // Notify the RecyclerView adapter that the data has changed
            adapter.notifyDataSetChanged()
        }.addOnFailureListener { exception ->
            Log.w(TAG, "Error getting documents: ", exception)
        }

        addNotice.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.fragment_container,AddNotice()).addToBackStack(null).commit()
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
         * @return A new instance of fragment AllNotices.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AllNotices().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}