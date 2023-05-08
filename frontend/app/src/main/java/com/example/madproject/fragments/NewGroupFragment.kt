package com.example.madproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.madproject.R
import com.example.madproject.models.NewGroup
import com.example.madproject.databinding.FragmentNewGroupBinding
import com.example.madproject.util.DatabaseFunctions

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NewGroupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewGroupFragment() : Fragment() {
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
        val binding: FragmentNewGroupBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_group, container, false)
        binding.lifecycleOwner = this


        //handle submit button
        binding.btnNewGroupCreate.setOnClickListener {
            val currentUser = DatabaseFunctions.getCurrentUser() ?: return@setOnClickListener

            val members = mutableMapOf<String, String>()
            members[currentUser.uid] = currentUser.displayName ?: currentUser.email ?: "Unknown"

            // Create a new group object with name, description, members, owner id and admins
            val newGroup = NewGroup(
                name = binding.etNewGroupName.text.toString(),
                description = binding.etNewGroupDescription.text.toString(),
                members = members,
                ownerId = currentUser.uid,
                admins = listOf(currentUser.uid)
            )

            // TODO: Validate group name and description

            // Add the group to the database
            DatabaseFunctions.createGroup(newGroup){
                if (it == null) return@createGroup
                //navigate to group view fragment
                val transaction = parentFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container, GroupViewFragment.newInstance(it))
                transaction.addToBackStack(null)
                transaction.commit()
            }

        }

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NewGroupFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewGroupFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}