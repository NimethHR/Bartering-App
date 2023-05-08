package com.example.madproject.fragments

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.madproject.R
import com.example.madproject.util.DatabaseFunctions
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.example.madproject.fragments.NewGroupFragment

@RunWith(AndroidJUnit4::class)
class NewGroupFragmentTest {

    @Test
    fun testCreateNewGroup() {
        // Launch the fragment
        val scenario = launchFragmentInContainer<NewGroupFragment>()

        // Wait for the fragment to be in the resumed state
        onView(withId(R.layout.fragment_new_group)).check(matches(isDisplayed()))

        // Fill in the group name and description
        onView(withId(R.id.et_new_group_name)).perform(typeText("Test Group"))
        onView(withId(R.id.et_new_group_description)).perform(typeText("This is a test group"))

        // Click the "Create" button
        onView(withId(R.id.btn_new_group_create)).perform(click())

        // Check that the group was created

        DatabaseFunctions.getCurrentUserFromFirestore() { user ->
            if (user?.joinedGroups == null || user.joinedGroups!!.isEmpty()) {
                assert(false, { "User has no joined groups" })
                return@getCurrentUserFromFirestore
            }
            for (group in user.joinedGroups!!.entries) {
                if (group.value == "Test Group") {
                    assert(true, { "Group was added successfully to user" })

                    DatabaseFunctions.getGroup(group.key) { group ->
                        assertEquals("Test Group", group?.name)
                        assertEquals("This is a test group", group?.description)
                        assertEquals(user.id , group?.ownerId)
                        assertEquals(user.id, group?.admins?.get(0))
                        assertEquals(user.id, group?.members?.get(user.id))

                    }
                    return@getCurrentUserFromFirestore

                }
            }
            assert(false, { "Group was not added to user" })


        }


    }
}
