package com.example.madproject.fragments

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.madproject.R
import com.example.madproject.models.Post
import com.example.madproject.posts.ViewPost
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditPostFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditPostFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var editBtn: Button
    private lateinit var cancelBtn: Button
    private lateinit var editFileBtn: Button
    private lateinit var editDesc: TextInputEditText
    private lateinit var radioButtonHave: RadioButton
    private lateinit var radioButtonNeed: RadioButton
    private lateinit var radioButtonFree: RadioButton
    private lateinit var editQuantity: EditText
    private lateinit var radioGroup: RadioGroup

    private var editTitle: String? = ""
    private var likeCount: Int? = 0
    private var fileIntent = Intent()
    private var selectedImageUri: Uri? = null
    private var imageDownloadUrl:String = ""

    private  var documentId: String? = null

    private val db = Firebase.firestore

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
        rootView = inflater.inflate(R.layout.fragment_edit_post, container, false)

        arguments?.let {
            documentId = it.getString("documentId")
        }

        editBtn = rootView.findViewById(R.id.edit_button)
        cancelBtn = rootView.findViewById(R.id.cancel_button)
        editDesc = rootView.findViewById(R.id.edit_post_desc)
        editQuantity = rootView.findViewById(R.id.editTextNumberDecimal2)
        radioButtonHave = rootView.findViewById(R.id.haveRadioButton)
        radioButtonNeed = rootView.findViewById(R.id.needRadioButton)
        radioButtonFree = rootView.findViewById(R.id.freeRadioButton)
        editFileBtn = rootView.findViewById(R.id.file_edit_btn)

        fetchData()

        cancelBtn.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setTitle("Discard Edit?")
            alertDialogBuilder.setMessage("All changes will be discarded")

            alertDialogBuilder.setPositiveButton("Discard") { dialog, which ->
                // Perform the delete operation here
                // Example: deleteItem()
            }
            alertDialogBuilder.setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }

        editBtn.setOnClickListener {
            editBtn.text = "Uploading..."
            uploadData()
        }

        editFileBtn.setOnClickListener {
            fileIntent = Intent()
                .setType("image/*")
                .setAction(Intent.ACTION_GET_CONTENT)

            startActivityForResult(Intent.createChooser(fileIntent, "Select a file"), 111)
        }
        return rootView
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 111 && resultCode == AppCompatActivity.RESULT_OK) {
            selectedImageUri = data?.data // The URI with the location of the file
        }
    }

    private fun uploadImage(imageUri: Uri, callback: (Boolean) -> Unit){
        val storage = Firebase.storage
        val storageRef = storage.reference
        val imageRef: StorageReference = storageRef.child("posts/$documentId.jpg")

        val uploadTask = imageRef.putFile(imageUri)
        uploadTask.addOnSuccessListener {
            val imageUrlTask = imageRef.downloadUrl // Image uploaded successfully
            imageUrlTask.addOnSuccessListener { uri ->
                imageDownloadUrl = uri.toString()
                callback(true)
            }


        }?.addOnFailureListener { exception ->
            // Handle any errors that occurred during the upload
            Log.e(ContentValues.TAG, "Error uploading image: ${exception.message}")

            callback(false)
        }
    }

    private fun fetchData(){
        val docRef = db.collection("posts").document(documentId!!)
        docRef.get()
            .addOnSuccessListener { result ->
                if (result.exists()){
                    Log.d("Tag", "Data fetched successfully in edit post")

                    val post = result.toObject(Post::class.java)
                    editTitle = post?.title
                    likeCount = post?.likes
                    editDesc.setText(post?.description)
                    when (post?.type) {
                        "Have" -> radioButtonHave.isChecked = true
                        "Need" -> radioButtonNeed.isChecked = true
                        "Free" -> radioButtonFree.isChecked = true
                    }
                    editQuantity.setText(post?.quantity.toString())
                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
    }

    private fun uploadData(){
        val desc = editDesc.text.toString()
        val number: Int = editQuantity.text.toString().toInt()

        radioGroup = rootView.findViewById(R.id.edit_radio_group)

        val selectedRadioButtonId = radioGroup.checkedRadioButtonId
        val selectedRadioButton = rootView.findViewById<RadioButton>(selectedRadioButtonId)
        val selectedType = selectedRadioButton.text.toString()

        val post = Post(editTitle, desc, selectedType , number, likeCount)

        db.collection("posts").document(documentId!!)
            .set(post)
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "Document edited with ID: $documentId")

                Log.d("Tag", "selected radio button: $selectedType")

                if (selectedImageUri != null){
                    uploadImage(selectedImageUri!!){success ->
                        if (success){
                            //image uploaded successfully
                            //image uploaded successfully
                            db.collection("posts").document(documentId!!)
                                .update("documentId", documentId, "imageDownloadUrl", imageDownloadUrl)


                            val viewPostFragment = ViewPostFragment()
                            val args = Bundle().apply {
                                putString("documentId", documentId)
                                putString("imageDownloadUrl", imageDownloadUrl)
                            }
                            Log.e(ContentValues.TAG, "Image URL Download from Edit post: $imageDownloadUrl")

                            val inflater: LayoutInflater = layoutInflater
                            val layout: View = inflater.inflate(R.layout.toast, rootView.findViewById(R.id.custom_toast_container))

                            val text: TextView = layout.findViewById(R.id.toast_text)
                            text.text = "Post Edited Successfully"
                            val toast = Toast(requireContext())
                            toast.duration = Toast.LENGTH_SHORT
                            toast.view = layout
                            toast.show()

                            viewPostFragment.arguments = args

                            requireActivity().supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, viewPostFragment)
                                .commit()
                        }else {
                            // Image upload failed, show an error message
                            Toast.makeText(requireContext(), "Error uploading image", Toast.LENGTH_SHORT).show()
                        }
                    }
                }


            }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditPost.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditPostFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}