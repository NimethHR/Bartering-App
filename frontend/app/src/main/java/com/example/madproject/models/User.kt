package com.example.madproject.models

data class User(
    var email: String? = null,
    var displayName: String? = null,
    var joinedGroups: Map<String, String>? = null,
    var id: String? = null,
    var groupInvites: Map<String, Map<String, String>>? = null
)
