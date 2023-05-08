package com.example.madproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madproject.R
import com.example.madproject.adapters.MessagesAdapter
import com.example.madproject.models.Message
import com.example.madproject.models.NewGroup
import com.example.madproject.util.DatabaseFunctions

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_ID = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [GroupViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GroupViewFragment() : Fragment() {
    // TODO: Rename and change types of parameters
    private var id: String? = null
    private  var group: NewGroup? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getString(ARG_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment by using binding
        val binding = com.example.madproject.databinding.FragmentGroupViewBinding.inflate(inflater, container, false)

        // handle group info
        DatabaseFunctions.getGroup(id ?: return binding.root) {
            group = it
            binding.toolbarGroupView.title = it?.name
        }

        // handle received messages
        val messageList = mutableListOf<Message>()
        binding.groupViewRecyclerView.adapter = MessagesAdapter(messageList)
        binding.groupViewRecyclerView.layoutManager = LinearLayoutManager(context)

        DatabaseFunctions.onNewMessage(id ?: return binding.root) {
            if (it == null || it.isEmpty()) return@onNewMessage
            messageList.addAll(it)
            binding.groupViewRecyclerView.adapter?.notifyDataSetChanged()
        }

        // handle sending messages
        binding.btnGroupViewSendMessage.setOnClickListener {

            val message = binding.etGroupViewMessage.text.toString()
            binding.etGroupViewMessage.text?.clear()

            val currentUser = DatabaseFunctions.getCurrentUser() ?: return@setOnClickListener

            // TODO: validate message

            DatabaseFunctions.sendMessage(
                Message(
                    message,
                    currentUser.uid,
                    currentUser.displayName ?: currentUser.email ?: "Unknown"
                ),
                id ?: return@setOnClickListener)
        }

        // handle menu items
        binding.toolbarGroupView.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.group_view_info -> {
                    parentFragmentManager.beginTransaction().replace(R.id.fragment_container, GroupInfoFragment.newInstance(id, group?.name, group?.description)).addToBackStack(null).commit()
                    true
                }
                R.id.group_view_edit -> {
                    parentFragmentManager.beginTransaction().replace(R.id.fragment_container, EditGroupFragment.newInstance(id, group?.name, group?.description)).addToBackStack(null).commit()
                    true
                }
                else -> false
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
         * @return A new instance of fragment GroupViewFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(id: String) =
            GroupViewFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_ID, id)
                }
            }
    }
}