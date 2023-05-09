package com.example.madproject.models

data class NewGroup(
    var name: String? = null,
    var description: String? = null,
    var members: Map<String, String>? = null,
    var ownerId: String? = null,
    var admins: List<String>? = null,
)
