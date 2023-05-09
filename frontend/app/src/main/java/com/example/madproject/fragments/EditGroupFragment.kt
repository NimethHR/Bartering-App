package com.example.madproject.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.madproject.R
import com.example.madproject.databinding.FragmentEditGroupBinding
import com.example.madproject.util.DatabaseFunctions
import com.example.madproject.util.StorageFunctions
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"

/**
 * A simple [Fragment] subclass.
 * Use the [EditGroupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditGroupFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var id: String? = null
    private var name: String? = null
    private var description: String? = null
    private var fileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getString(ARG_PARAM1)
            name = it.getString(ARG_PARAM2)
            description = it.getString(ARG_PARAM3)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding : FragmentEditGroupBinding = FragmentEditGroupBinding.inflate(inflater, container, false)

        binding.groupEditToolbar.title = name
        binding.etEditGroupDescription.setText(description)

        Glide.with(binding.root.context)
            .load(StorageFunctions.getGroupImageUrl(id!!))
            .circleCrop()
            .placeholder(R.drawable.baseline_groups_24)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(binding.ivEditGroupImage)

        binding.btnEditGroupSave.setOnClickListener() {
            val newDescription = binding.etEditGroupDescription.text.toString()

            DatabaseFunctions.updateGroup(id ?: return@setOnClickListener, newDescription)
            StorageFunctions.uploadImage(fileUri ?: return@setOnClickListener, "groups", id ?: return@setOnClickListener)
            activity?.onBackPressed()
        }

        binding.btnEditGroupCancel.setOnClickListener() {
            activity?.onBackPressed()
        }

        binding.btnEditGroupChangePicture.setOnClickListener() {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent,"Select Picture"), 22)

        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val binding : FragmentEditGroupBinding = FragmentEditGroupBinding.inflate(layoutInflater)
        if (requestCode == 22 && resultCode == RESULT_OK && data != null && data.data != null) {
            fileUri = data.data

        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditGroupFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(id: String?, name: String?, description: String?) =
            EditGroupFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, id)
                    putString(ARG_PARAM2, name)
                    putString(ARG_PARAM3, description)
                }
            }
    }
}