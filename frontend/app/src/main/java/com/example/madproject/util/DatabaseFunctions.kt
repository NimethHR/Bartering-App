package com.example.madproject.util

import android.text.format.DateUtils
import com.example.madproject.models.GroupInvite
import com.example.madproject.models.Message
import com.example.madproject.models.NewGroup
import com.example.madproject.models.User
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

object DatabaseFunctions {

    // create a new group on firestore using given group object
    fun createGroup(newGroup: NewGroup, onComplete: (groupRefId: String?) -> Unit) {
        val db = Firebase.firestore
        val groupRef = db.collection("groups").document()
        groupRef.set(newGroup).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val groupRefId = groupRef.id
                addGroupToUser(newGroup, groupRefId)
                onComplete(groupRefId)
            } else {
                onComplete(null)
            }
        }
    }

    //get current user from auth
    fun getCurrentUser(): FirebaseUser?{
        val auth = Firebase.auth
        return auth.currentUser
    }

    //add group to user's firestore document
    private fun addGroupToUser(newGroup: NewGroup, groupId: String) {
        val db = Firebase.firestore
        val userRef = db.collection("users").document(newGroup.ownerId!!)
        userRef.update("joinedGroups.$groupId", newGroup.name)
    }


    // get current user's document from firestore
    fun getCurrentUserFromFirestore(onComplete: (result: User?) -> Unit) {
        val db = Firebase.firestore
        val currentUser = getCurrentUser()
        if (currentUser == null){
            onComplete(null)
            return
        }
        val userRef = db.collection("users").document(getCurrentUser()!!.uid)

        userRef.get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(task.result.toObject<User>())
                } else {
                    onComplete(null)
                }
            }
    }

    // send a message to a group
    fun sendMessage(message: Message, groupId: String) {
        val db = Firebase.firestore
        val groupRef = db.collection("groups").document(groupId).collection("messages")
        groupRef.add(message)
    }

    // get all messages from a group and listen to new messages
    fun onNewMessage(groupId: String, onNewMessage: (messages: List<Message>) -> Unit) {
        val db = Firebase.firestore
        val query = db.collection("groups").document(groupId).collection("messages")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(1)
        var firstMessageReceived = false
        query.addSnapshotListener { value, error ->
            if (error != null) {
                return@addSnapshotListener
            }

            if (value == null || value.isEmpty) {
                firstMessageReceived = true
                return@addSnapshotListener
            }

            val messageSnap = value?.documents?.get(0)

            if (!firstMessageReceived) {
                firstMessageReceived = true
                val query2 = db.collection("groups").document(groupId).collection("messages")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .limit(10)
                    .startAfter(messageSnap?.toObject<Message>()?.timestamp)
                query2.get().addOnCompleteListener {
                    if (it.isSuccessful) {
                        if (it.result.isEmpty) {
                            onNewMessage(listOf(messageSnap?.toObject<Message>()!!))
                            return@addOnCompleteListener
                        }
                        var messages = it.result.toObjects(Message::class.java)
                        messages = messages.reversed()
                        messages.add(messageSnap?.toObject<Message>()!!)
                        onNewMessage(messages)
                    }
                }
                return@addSnapshotListener
            }

            onNewMessage(listOf(messageSnap?.toObject<Message>()!!))
        }
    }

    // get a group document by id
    fun getGroup(groupId: String, onComplete: (result: NewGroup?) -> Unit) {
        val db = Firebase.firestore
        val groupRef = db.collection("groups").document(groupId)

        groupRef.get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(task.result.toObject<NewGroup>())
                } else {
                    onComplete(null)
                }
            }
    }

    // update a group description
    fun updateGroup(groupId: String, description: String) {
        val db = Firebase.firestore
        val groupRef = db.collection("groups").document(groupId)

        groupRef.update("description", description)

    }

    // delete a group by id
    fun deleteGroup(groupId: String, onComplete: (success: Boolean) -> Unit) {
        val db = Firebase.firestore

        getGroup(groupId) { group ->
            if (group == null || group.members == null) {
                onComplete(false)
                return@getGroup
            }

            // Create a batched write object
            val batch = db.batch()

            // Delete group document
            val groupRef = db.collection("groups").document(groupId)
            batch.delete(groupRef)

            // Update joinedGroups field for each member
            val members = group.members ?: mapOf()
            for (userId in members.keys) {
                val userRef = db.collection("users").document(userId)
                batch.update(userRef, "joinedGroups.$groupId", FieldValue.delete())
            }

            // Commit the batched write
            batch.commit().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true)
                } else {
                    onComplete(false)
                }
            }
        }
    }

    // get user by user's display name
    fun getUserByDisplayName(displayName: String, onComplete: (result: User?) -> Unit) {
        val db = Firebase.firestore
        val userRef = db.collection("users").whereEqualTo("displayName", displayName)

        userRef.get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.result.isEmpty) {
                        onComplete(null)
                        return@addOnCompleteListener
                    }
                    val doc = task.result.documents[0]
                    val user = doc.toObject<User>()
                    user?.id = doc.id
                    onComplete(user)
                } else {
                    onComplete(null)
                }
            }
    }

    // invite a user to a group
    fun inviteUserToGroup(groupId: String, groupName: String, inviteeId: String, inviterId: String, inviterName: String, onComplete: (success: Boolean) -> Unit) {
        val db = Firebase.firestore

        val userRef = db.collection("users").document(inviteeId)

        // add new invite to users groupInvites map
        userRef.update("groupInvites.$groupId", mapOf(
            "groupName" to groupName,
            "inviterId" to inviterId,
            "inviterName" to inviterName
        )).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onComplete(true)
            } else {
                onComplete(false)
            }
        }
    }

    // accept a group invite
    fun acceptGroupInvite(groupId: String, groupName: String, onComplete: (success: Boolean) -> Unit) {
        val db = Firebase.firestore
        val batch = db.batch()

        val currentUser = getCurrentUser()!!

        val userRef = db.collection("users").document(currentUser.uid)
        batch.update(userRef, "groupInvites.$groupId", FieldValue.delete(), "joinedGroups.$groupId", groupName)

        val groupRef = db.collection("groups").document(groupId)
        batch.update(groupRef, "members.${currentUser.uid}", currentUser.displayName ?: currentUser.email )

        batch.commit().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onComplete(true)
            } else {
                onComplete(false)
            }
        }
    }

    // decline a group invite
    fun declineGroupInvite(groupId: String, onComplete: (success: Boolean) -> Unit) {
        val db = Firebase.firestore
        val userRef = db.collection("users").document(getCurrentUser()!!.uid)

        // remove invite from user
        userRef.update("groupInvites.$groupId", FieldValue.delete())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true)
                } else {
                    onComplete(false)
                }
            }
    }

    // get chat activity report
    fun getChatActivity(groupId: String, onComplete: (result: List<Pair<String,Long>>?) -> Unit) {
        val db = Firebase.firestore
        val messagesRef = db.collection("groups").document(groupId).collection("messages")

        val tasks = mutableListOf<Pair<String, Task<AggregateQuerySnapshot>>>() // pair of date and task

        //get today midnight milliseconds


        for (i in 0..10) {
            val millisFrom = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_MONTH, -i)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
            }.timeInMillis

            val millistTo = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_MONTH, -i)
                set(Calendar.HOUR_OF_DAY, 23)
                set(Calendar.MINUTE, 59)
                set(Calendar.SECOND, 59)
            }.timeInMillis


            tasks.add(Pair(SimpleDateFormat("yyyy-MM-dd").format(Date(millisFrom)), messagesRef
                .whereGreaterThanOrEqualTo("timestamp", Date(millisFrom))
                .whereLessThanOrEqualTo("timestamp", Date(millistTo))
                .count()
                .get(AggregateSource.SERVER)))

        }

        Tasks.whenAllComplete(tasks.map { it.second }).addOnCompleteListener {
            if (it.isSuccessful) {
                val result = mutableListOf<Pair<String, Long>>()
                for (i in 0..10) {
                    val task = tasks[i]
                    val date = task.first
                    val count = task.second.result.count
                    result.add(Pair(date, count))
                }
                onComplete(result)
            } else {
                onComplete(null)
            }
        }
    }

}