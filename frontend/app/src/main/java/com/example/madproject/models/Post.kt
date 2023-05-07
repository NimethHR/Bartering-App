package com.example.madproject.models

data class Post (
    var title: String? = null,
    var description: String? = null,
    var type: String? = null,
    var quantity: Int? = 0,
    var likes: Int? = 0
)