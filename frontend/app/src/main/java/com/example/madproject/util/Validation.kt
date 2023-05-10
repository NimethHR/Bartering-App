package com.example.madproject.util

import com.example.madproject.models.NewGroup
import com.example.madproject.models.Post
import com.example.madproject.models.ValidationResult

object Validation {

    fun validateGroup(group: NewGroup): Map<String, ValidationResult> {
        var out = mapOf<String, ValidationResult>()

        if (group.name.isNullOrEmpty()) {
            out += "name" to ValidationResult(false, "Name cannot be empty")
        } else if (group.name!!.length < 8 || group.name!!.length > 20) {
            out += "name" to ValidationResult(
                false,
                "Name must be between 8 and 20 characters long"
            )
        } else if (!Regex("^[a-zA-Z0-9_-]*\$").matches(group.name!!)) {
            out += "name" to ValidationResult(
                false,
                "Name can only contain letters, numbers, underscores and dots"
            )
        } else {
            out += "name" to ValidationResult(true, null)
        }


        if (!group.description.isNullOrEmpty() && group.description!!.length > 150) {
            out += "description" to ValidationResult(
                false,
                "Description cannot be longer than 150 characters"
            )
        } else {
            out += "description" to ValidationResult(true, null)
        }

        return out
    }

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