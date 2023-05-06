package com.example.madproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.madproject.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GroupListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GroupListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var tabLayout : TabLayout
    private lateinit var view: View
    private lateinit var toolbar: MaterialToolbar

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
        view = inflater.inflate(R.layout.fragment_group_list, container, false)

        tabLayout = view.findViewById(R.id.group_list_tab_layout)
        toolbar = view.findViewById(R.id.group_list_toolbar)

        tabLayout.selectTab( tabLayout.getTabAt(0) )
        childFragmentManager.beginTransaction().replace(R.id.group_list_fragment_container, JoinedGroupsFragment()).commit()


        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tabSelected(tab)
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Do nothing
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {
                tabSelected(tab)
            }
            fun tabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        childFragmentManager.beginTransaction().replace(R.id.group_list_fragment_container, JoinedGroupsFragment()).commit()
                    }
                    1 -> {
                        childFragmentManager.beginTransaction().replace(R.id.group_list_fragment_container, GroupInvitesFragment()).commit()
                    }
                }
            }
        })

        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.group_list_new_group -> {
                    parentFragmentManager.beginTransaction().replace(R.id.fragment_container, NewGroupFragment()).addToBackStack(null).commit()
                    true
                }
                else -> false
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
         * @return A new instance of fragment GroupListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GroupListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}