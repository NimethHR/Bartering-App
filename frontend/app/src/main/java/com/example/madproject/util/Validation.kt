package com.example.madproject.util

import com.example.madproject.models.Post
import com.example.madproject.models.ValidationResult

object Validation {

    private const val MAX_TITLE_LENGTH = 150
    private const val MAX_DESCRIPTION_LENGTH = 750

    fun validatePost(post: Post): Map<String, Boolean> {
        val validationMap = mutableMapOf<String, Boolean>()

        val title = post.title
        val description = post.description

        if (title != null && title.length > MAX_TITLE_LENGTH) {
            validationMap["title"] = false // Title length exceeds the maximum length
        } else {
            validationMap["title"] = true // Title is valid
        }

        if (description != null && description.length > MAX_DESCRIPTION_LENGTH) {
            validationMap["description"] = false // Description length exceeds the maximum length
        } else {
            validationMap["description"] = true // Description is valid
        }

        return validationMap
    }

}