package com.example.madproject

import android.content.ContentValues.TAG
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.firestore.FirebaseFirestore
import com.example.madproject.fragments.ViewPostFragment




import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.junit.After
import org.junit.Before

@RunWith(AndroidJUnit4::class)
class LikeUpdateInstrumentedTest {

    private lateinit var db: FirebaseFirestore
    private lateinit var documentId: String
    private var likeCount: Int = 0

    @Before
    fun setup() {
        // Initialize Firebase and any necessary dependencies
        db = FirebaseFirestore.getInstance()

        // Set up any initial data required for the test
        documentId = "0edjb7qCtOmCPvj6vHJV"
        likeCount = 57
    }

    @Test
    fun testLikeUpdate() {
        // Call the function to be tested
        val viewPostFragment = ViewPostFragment()
        viewPostFragment.likeUpdate()

        // Retrieve the updated like count from Firebase and verify if it matches the expected value
        db.collection("posts").document(documentId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val updatedLikeCount = documentSnapshot.getLong("likes")?.toInt()

                // Assert that the updated like count matches the expected value
                assertEquals(likeCount, updatedLikeCount)
            }
            .addOnFailureListener { exception ->
                // Test failed, log the error
                Log.e(TAG, "Failed to retrieve document from Firebase: $exception")
            }
    }

    @After
    fun cleanup() {
        // Clean up any resources after the test
    }
}