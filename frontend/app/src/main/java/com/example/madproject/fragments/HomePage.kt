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
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madproject.R
import com.example.madproject.adapter.PostAdapter
import com.example.madproject.models.Post
import com.example.madproject.models.PostUnit
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

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
    private lateinit var searchEditText: TextInputEditText
    private val searchResultsList = mutableListOf<PostUnit>()
    private lateinit var adapter: PostAdapter

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
        adapter = PostAdapter(postsList)
        searchEditText = rootView.findViewById(R.id.search_bar_edit_text)
        val searchButton: Button = rootView.findViewById(R.id.search_button)
        val toolbar : androidx.appcompat.widget.Toolbar = rootView.findViewById(R.id.home_page_toolbar)

        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.admin_dashboard -> {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, AdminDashboard())
                        .commit()
                    true
                }
                R.id.add_feedback -> {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, AddFeedback())
                        .commit()
                    true
                }
                else -> false
            }
        }

        adapter.setOnItemClickListener { documentId ->
            var imageURL:String? = "ghjhk"
            var docRef = db.collection("posts").document(documentId)
            docRef.get()
                .addOnSuccessListener { result ->
                    if (result.exists()){
                        imageURL = result.getString("imageDownloadUrl")

                        val viewPostFragment = ViewPostFragment()
                        val args = Bundle().apply {
                            putString("documentId", documentId)
                            putString("imageURL", imageURL)
                            Log.w(TAG, "imageURl value in HomePage:  $imageURL")

                        }

                        viewPostFragment.arguments = args
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, viewPostFragment)
                            .commit()

                    }else{
                        val inflater: LayoutInflater = layoutInflater
                        val layout: View = inflater.inflate(R.layout.toast, rootView.findViewById(R.id.custom_toast_container))

                        val text: TextView = layout.findViewById(R.id.toast_text)
                        text.text = "Could not load image"
                        val toast = Toast(requireContext())
                        toast.duration = Toast.LENGTH_SHORT
                        toast.view = layout
                        toast.show()
                    }
                }
        }

        searchButton.setOnClickListener {
            val query = searchEditText.text.toString()
//            adapter = PostAdapter(searchResultsList)

            searchDocs(query)
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
        return rootView
    }

    private fun searchDocs(query: String) {
        val queryText = query.lowercase()

        val searchQuery = db.collection("posts")
            .whereEqualTo("title", query)

        searchQuery.get()
            .addOnSuccessListener { querySnapshot ->
                // Process the search results
                val searchResults = querySnapshot.toObjects<PostUnit>()
                // Update the RecyclerView or display search results
                Log.e(TAG, "Search resuslts: $searchResults")

                searchResultsList.clear()
                searchResultsList.addAll(searchResults)
                adapter.updateData(searchResultsList)

                adapter.notifyDataSetChanged()

                // Clear the search query
                searchEditText.text?.clear()
            }
            .addOnFailureListener { exception ->
                searchEditText.setText("No resuslts matching $query")
            }
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
