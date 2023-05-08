package com.example.madproject.models

import com.google.firebase.Timestamp

data class Message(
    var content : String? = null,
    var senderName : String? = null,
    var senderId : String? = null,
    var timestamp : Timestamp? = Timestamp.now()

)
