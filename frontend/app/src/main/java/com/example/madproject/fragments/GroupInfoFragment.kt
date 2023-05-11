package com.example.madproject.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madproject.R
import com.example.madproject.databinding.FragmentGroupInfoBinding
import com.example.madproject.util.DatabaseFunctions

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.madproject.adapters.ChatActivityAdapter
import com.example.madproject.adapters.GroupInvitesAdapter
import com.example.madproject.util.StorageFunctions

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"

/**
 * A simple [Fragment] subclass.
 * Use the [GroupInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GroupInfoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var id: String? = null
    private var name: String? = null
    private var description: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getString(ARG_PARAM1)
            name = it.getString(ARG_PARAM2)
            description = it.getString(ARG_PARAM3)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding: FragmentGroupInfoBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_group_info, container, false)

        binding.groupInfoToolbar.title = name
        binding.tvGroupViewName.text = name
        binding.tvGroupViewDescription.text = description

        Glide.with(binding.root.context)
            .load(StorageFunctions.getGroupImageUrl(id!!))
            .circleCrop()
            .placeholder(R.drawable.baseline_groups_24)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(binding.ivGroupImage)

        binding.groupInfoToolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.group_info_delete -> {
                    val alertDialog = AlertDialog.Builder(context)
                        .setTitle("Confirm Delete")
                        .setMessage("Are you sure you want to delete this group?")
                        .setPositiveButton("Yes") { _, _ ->
                            DatabaseFunctions.deleteGroup(id!!){ success ->
                                if (!success) {
                                    return@deleteGroup
                                }

                                val transaction = parentFragmentManager.beginTransaction()
                                transaction.replace(R.id.fragment_container, GroupListFragment())
                                transaction.addToBackStack(null)
                                transaction.commit()
                            }
                        }
                        .setNegativeButton("No", null)
                        .create()
                    alertDialog.show()
                    true
                }
                else -> false
            }
        }

        binding.btnGroupViewInvite.setOnClickListener {
            DatabaseFunctions.getUserByDisplayName(binding.etGroupViewInvite.text.toString()) { user ->
                if (user == null) {
                    binding.etGroupViewInvite.error = "User not found"
                    return@getUserByDisplayName
                }

                val currentUser = DatabaseFunctions.getCurrentUser()

                DatabaseFunctions.inviteUserToGroup(id!!, name!!, user.id!!, currentUser?.uid!!, currentUser.displayName ?: currentUser.email!!) { success ->

                    if (!success) {
                        return@inviteUserToGroup
                    }

                    binding.etGroupViewInvite.text?.clear()
                }
            }
        }

        DatabaseFunctions.getChatActivity(id!!) { chatActivity ->
            if (chatActivity == null) {
                return@getChatActivity
            }

            val adapter = ChatActivityAdapter(chatActivity)
            val recyclerView: RecyclerView = binding.rvGroupMessageActivity
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(context)
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
         * @return A new instance of fragment GroupInfoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic fun newInstance(id: String?, name: String?, description: String?) =
                GroupInfoFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, id)
                        putString(ARG_PARAM2, name)
                        putString(ARG_PARAM3, description)
                    }
                }
    }
}