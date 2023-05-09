package com.example.madproject.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.example.madproject.R
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.madproject.MainActivity
import com.example.madproject.posts.EditPost
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.util.zip.Inflater
import android.view.MenuInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import javax.annotation.Nullable

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ViewPostFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewPostFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private  var documentId: String? = null
    private  var imageDownloadUrl: String? = null
    private var isImageLoaded = false

    private lateinit var likeBtn: Button
    private lateinit var titleTextView: MaterialTextView
    private lateinit var descTextView: MaterialTextView
    private lateinit var viewPosType: MaterialTextView
    private lateinit var viewPostQuantity: MaterialTextView
    private lateinit var postImage: ImageView


    var likeCount: Int = 0
    var isLiked = false
    var quantity: Int = 0

    private val db = Firebase.firestore
    private val storage = Firebase.storage

    private lateinit var rootView: View


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
        rootView = inflater.inflate(R.layout.fragment_view_post, container, false)

        arguments?.let {
            documentId = it.getString("documentId")
            imageDownloadUrl = it.getString("imageDownloadUrl")
        }



        Log.d("Tag", "document id in ViewPost: $documentId")

        val toolbar = rootView.findViewById<Toolbar>(R.id.create_toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        setHasOptionsMenu(true)

        likeBtn = rootView.findViewById(R.id.likeBtn)
        titleTextView = rootView.findViewById(R.id.view_post_title)
        descTextView = rootView.findViewById(R.id.view_post_desc)
        viewPosType = rootView.findViewById(R.id.view_post_type)
        viewPostQuantity = rootView.findViewById(R.id.view_post_quantity)
        postImage = rootView.findViewById(R.id.imageView3)

        likeBtn.setOnClickListener {
            if (isLiked){
                isLiked = false
                likeCount--
                if (likeCount!=0){
                    likeBtn.text = "Liked " + likeCount
                }
                else{
                    likeBtn.text = "Like"
                }
            }
            else{
                likeCount++
                isLiked = true
                likeBtn.text = "Liked $likeCount"
            }
            likeUpdate()
        }
        fetchData()
        return rootView
    }

    private fun likeUpdate(){
        db.collection("posts").document(documentId!!)
            .update("likes", likeCount)
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "Like count updated for Likes = $likeCount")
            }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.create_post_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.toolbar_edit -> {
                // Handle Edit button click
                val editPostFragment = EditPostFragment()

                val args = Bundle().apply {
                    putString("documentId", documentId)
                }

                editPostFragment.arguments = args

                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, editPostFragment)
                    .commit()
                true
            }
            R.id.toolbar_delete -> {

                val alertDialogBuilder = AlertDialog.Builder(requireContext())
                alertDialogBuilder.setTitle("Confirm Delete")
                alertDialogBuilder.setMessage("Are you sure you want to delete this post?")

                //TODO - Add +ve and -ve options

                alertDialogBuilder.setPositiveButton("Delete") { dialog, which ->
                    // Perform the delete operation here
                    // Example: deleteItem()

                    // Handle Delete button click
                    val docRef = db.collection("posts").document(documentId!!)
                    docRef
                        .delete()
                        .addOnSuccessListener {
                            Log.d(TAG, "Post successfully deleted from firestore")

                            val storage = Firebase.storage
                            val storageRef = storage.reference
                            val imageRef: StorageReference? = storageRef.child("posts/$documentId.jpg")

                            imageRef?.delete()
                                ?.addOnSuccessListener {
                                    Log.d(TAG, "Image file successfully deleted from cloud storage")

                                    val inflater: LayoutInflater = layoutInflater
                                    val layout: View = inflater.inflate(R.layout.toast, rootView.findViewById(R.id.custom_toast_container))

                                    val text: TextView = layout.findViewById(R.id.toast_text)
                                    text.text = "Post Deleted Successfully"
                                    val toast = Toast(requireContext())
                                    toast.duration = Toast.LENGTH_SHORT
                                    toast.view = layout
                                    toast.show()


//                                    val homePage = MainActivity()
//
//                                    requireActivity().supportFragmentManager.beginTransaction()
//                                        .replace(R.id.fragment_container, homePage)
//                                        .commit()

                                    val intent = Intent(requireContext(), MainActivity::class.java)
                                    startActivity(intent)

                                }?.addOnFailureListener {
                                    Log.d(TAG, "Error deleting Image file from cloud storage")
                                }
                        }
                        .addOnFailureListener { e -> Log.w(TAG, "Error deleting Post", e) }

                }
                alertDialogBuilder.setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }

                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadImage() {
        postImage.setImageResource(R.drawable.sample1_sofa)

        Glide.with(requireContext())
            .load(imageDownloadUrl)
            .placeholder(R.drawable.loading_bar)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    @Nullable e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    // Handle the case when image loading fails, e.g., show an error message
                    Log.w(TAG, "################ Image Loading failed #################")
                    return false
                }
                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    // Image loading succeeded, update the flag
                    isImageLoaded = true
//                    updateImageVisibility()
                    return false
                }
            })
            .into(postImage)
    }

    private fun fetchData(){
        val storageRef = storage.reference
        val imageRef = storageRef.child("posts/$documentId.jpg")
        val ONE_MEGABYTE: Long = 1024 * 1024

        val docRef = db.collection("posts").document(documentId!!)

        if (!imageDownloadUrl.isNullOrEmpty()) {
            loadImage()
            Log.w(TAG, "imageDownloadUrl has a value, Loading Image.......")
        }

        docRef.get()
            .addOnSuccessListener { result ->
                if (result.exists()){
                    val title = result.getString("title")
                    val desc = result.getString("description")
                    val type = result.getString("type")
                    val longLikeCount = result.getLong("likes")
                    val longQuantity = result.getLong("quantity")

                    quantity =longQuantity?.toInt()?: 0

                    likeCount = longLikeCount?.toInt()?: 0

                    titleTextView.text = title
                    descTextView.text = desc
                    viewPostQuantity.text = "Amount of units: " + quantity.toString()
                    viewPosType.text = "Post Type: " + type
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ViewPostFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ViewPostFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}