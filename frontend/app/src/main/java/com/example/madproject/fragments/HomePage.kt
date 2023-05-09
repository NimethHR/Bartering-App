package com.example.madproject.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madproject.R
import com.example.madproject.adapter.PostAdapter
import com.example.madproject.models.PostUnit
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.DocumentSnapshot

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomePage.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomePage : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val db = Firebase.firestore

    private lateinit var rootView: View
    private val postsList: MutableList<PostUnit> = mutableListOf()
//    private lateinit var adapter: PostAdapter

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
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home_page, container, false)

        val recyclerView: RecyclerView = rootView.findViewById(R.id.home_page_recycler)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(requireContext(), 2)
        val adapter: PostAdapter = PostAdapter(postsList)

//        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
//        val postAdapter = PostAdapter(postsList)

        adapter.setOnItemClickListener { documentId ->

            val viewPostFragment = ViewPostFragment()
            val args = Bundle().apply {
                putString("documentId", documentId)
            }

            viewPostFragment.arguments = args

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, viewPostFragment)
                .commit()
        }

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        val postCollectionRef = db.collection("posts")
        Log.e(TAG, "postCollectionRef has: $postCollectionRef")


        postCollectionRef.get()
            .addOnSuccessListener { result ->
                for (documentSnapshot in result.documents) {
                    val documentId = documentSnapshot.id
                    val post = documentSnapshot.toObject(PostUnit::class.java)
                    if (post != null) {
                        postsList.add(post)
                    }
                }
                adapter.notifyDataSetChanged()

            }.addOnFailureListener { exception ->
                // Handle any errors that occurred while fetching data
                Log.e(TAG, "Error getting posts: $exception")
            }


//        fetchData()
        return rootView
    }

//    private fun fetchData() {
//        val postCollectionRef = db.collection("posts")
//        postCollectionRef.get()
//            .addOnSuccessListener { result ->
//                for (documentSnapshot in result.documents) {
//                    val post = documentSnapshot.toObject(PostUnit::class.java)
//                    if (post != null) {
//                        postsList.add(post)
//                    }
//                }
//                if (::adapter.isInitialized) {
//                    adapter.notifyDataSetChanged()
//                }
//            }.addOnFailureListener { exception ->
//                // Handle any errors that occurred while fetching data
//                Log.e(TAG, "Error getting posts: $exception")
//            }
//    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomePage.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomePage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
