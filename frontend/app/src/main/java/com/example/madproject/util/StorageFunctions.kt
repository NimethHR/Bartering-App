package com.example.madproject.util

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage

object StorageFunctions {
    fun uploadImage(imageUri: Uri, bucket: String, fileName: String) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val imageRef = storageRef.child("$bucket/$fileName")

        imageRef.putFile(imageUri)
    }

    fun getGroupImageUrl(groupId: String): Uri {
        val url = "https://firebasestorage.googleapis.com/v0/b/mad-bartering-app.appspot.com/o/groups%2F$groupId?alt=media"
        return Uri.parse(url)
    }

    fun getUserImageUrl(userId: String): Uri {
        val url = "https://firebasestorage.googleapis.com/v0/b/mad-bartering-app.appspot.com/o/users%2F$userId?alt=media"
        return Uri.parse(url)
    }
}