package com.example.madproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madproject.R
import com.example.madproject.adapters.GroupInvitesAdapter
import com.example.madproject.adapters.GroupsAdapter
import com.example.madproject.models.GroupInvite
import com.example.madproject.util.DatabaseFunctions

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GroupInvitesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GroupInvitesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var view: View

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
        view = inflater.inflate(R.layout.fragment_group_invites, container, false)

        DatabaseFunctions.getCurrentUserFromFirestore {
                user ->
            if (user != null) {
                val groupInvites = user.groupInvites
                val groupInviteList = groupInvites?.map { GroupInvite(
                    groupId = it.key,
                    groupName = it.value["groupName"],
                    inviterId = it.value["inviterId"],
                    inviterName = it.value["inviterName"]
                ) }?.toList()

                val recyclerView: RecyclerView = view.findViewById(R.id.groupInvitesRecyclerView)
                val adapter = GroupInvitesAdapter(groupInviteList ?: listOf())
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(view.context)
            }
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
         * @return A new instance of fragment GroupInvitesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GroupInvitesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}