package com.example.madproject.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.madproject.R
import com.google.android.material.textfield.TextInputEditText

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.madproject.models.Post
import com.example.madproject.posts.ViewPost
import com.example.madproject.util.Validation
import com.google.firebase.auth.ktx.auth
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
 * Use the [CreatePostFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreatePostFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var createPostTitle: TextInputEditText
    private lateinit var createPostDesc: TextInputEditText
    private lateinit var createPostType: RadioButton
    private lateinit var createPostQuantity: EditText
    private lateinit var uploadBtn: Button
    private lateinit var cancelBtn: Button
    private lateinit var browseBtn: Button
    private lateinit var radioGroup: RadioGroup

    private var likeCount: Int = 0
    var documentId: String? = null
    private var fileIntent = Intent()
    private var selectedImageUri: Uri? = null
    private var imageDownloadUrl:String = ""
    private var selectedType: String = ""
    private lateinit var rootView: View
    var auth = Firebase.auth
    var user = auth.currentUser

//    lateinit var post:Post


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
        rootView = inflater.inflate(R.layout.fragment_create_post, container, false)

        createPostTitle = rootView.findViewById(R.id.create_post_title_input)
        createPostDesc = rootView.findViewById(R.id.create_post_desc_input)
        createPostType = rootView.findViewById(R.id.haveRadioButton)
        createPostQuantity = rootView.findViewById(R.id.editTextNumberDecimal2)
        uploadBtn = rootView.findViewById(R.id.upload_post_btn)
        cancelBtn = rootView.findViewById(R.id.cancel_button)
        browseBtn = rootView.findViewById(R.id.file_browse_btn)
        radioGroup = rootView.findViewById(R.id.type_radio_group)

//        if (radioGroup.checkedRadioButtonId <= 0) {
//            Toast.makeText(requireContext(), "Choose Radio Button Please", Toast.LENGTH_SHORT).show();
//        }

        uploadBtn.setOnClickListener {

//            var result = Validation.validatePost(post)
//            if (result["title"] == false){
//                createPostTitle.error = "Please enter title between 0 and 150 characters"
//            }
//            if (result["description"] == false){
//                createPostDesc.error = "Please enter description between 0 and 150 characters"
//            }

            if (createPostTitle.text!!.isEmpty()){
                createPostTitle.error = "Please enter a value"
            }
            else if (createPostDesc.text!!.isEmpty()){
                createPostDesc.error = "Please enter a value"
            }
            else if (createPostQuantity.text!!.isEmpty()){
                createPostQuantity.error = "Please enter a value"
            }
            else{
                uploadBtn.text = "Uploading..."
                uploadData()
            }

        }

        cancelBtn.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(requireActivity())
            alertDialogBuilder.setTitle("Discard Post?")
            alertDialogBuilder.setMessage("Data will be discarded. You cannot undo this process")

            //TODO - Add +ve and -ve options

            alertDialogBuilder.setPositiveButton("Discard") { dialog, which ->
                // Perform the delete operation here
                // Example: deleteItem()
                val homePageFragment = HomePage()

                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, homePageFragment)
                    .commit()

            }
            alertDialogBuilder.setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }

        browseBtn.setOnClickListener {
            val inflater: LayoutInflater = layoutInflater
            val layout: View = inflater.inflate(R.layout.toast, container, false)

            val text: TextView = layout.findViewById(R.id.toast_text)
            text.text = "Only .png .jpg  .webp images are supported"
            val toast = Toast(requireContext())
            toast.duration = Toast.LENGTH_LONG
            toast.view = layout
            toast.show()

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

//            if (imageUri != null) {
//                // Handle the image URI (e.g., upload it to Firebase Storage)
//                if (documentId != null){
//                    uploadImage(imageUri)
//                }
//            }
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
            Log.e(TAG, "Error uploading image: ${exception.message}")

            callback(false)
        }
    }

    private fun uploadData(){
        val db = Firebase.firestore

        val title = createPostTitle.text.toString()
        val desc = createPostDesc.text.toString()
        val number: Int = createPostQuantity.text.toString().toInt()

        val selectedRadioButtonId = radioGroup.checkedRadioButtonId
        val selectedRadioButton = rootView.findViewById<RadioButton>(selectedRadioButtonId)
        selectedType = selectedRadioButton.text.toString()


        val post = Post(title, desc, selectedType , number, likeCount)

        db.collection("posts")
            .add(post)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                documentId = documentReference.id

                if (selectedImageUri != null){
                    uploadImage(selectedImageUri!!){success ->
                        if (success){
                            //image uploaded successfully
                            db.collection("posts").document(documentId!!)
                                .update("documentId", documentId,"user", user?.uid, "imageDownloadUrl", imageDownloadUrl)

                            val viewPostFragment = ViewPostFragment()
                            val args = Bundle().apply {
                                putString("documentId", documentId)
                                putString("imageDownloadUrl", imageDownloadUrl)
                            }
                            Log.e(TAG, "Image URL Download: $imageDownloadUrl")

                            val inflater: LayoutInflater = layoutInflater
                            val layout: View = inflater.inflate(R.layout.toast, rootView.findViewById(R.id.custom_toast_container))

                            val text: TextView = layout.findViewById(R.id.toast_text)
                            text.text = "Post Uploaded Successfully"
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
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CreatePostFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreatePostFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}