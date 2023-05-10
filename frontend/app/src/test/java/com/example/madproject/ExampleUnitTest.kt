package com.example.madproject

import com.example.madproject.models.Post
import com.example.madproject.util.Validation.validatePost
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    private val MAX_TITLE_LENGTH = 150
    private val MAX_DESCRIPTION_LENGTH = 750


    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testValidatePost() {
        val post = Post("Lorem ipsum", "Description", "Type", 100, 50)

        val result = validatePost(post)

        assertEquals(true, result["title"])
        assertEquals(true, result["description"])
    }

    @Test
    fun testValidatePostWithLongTitle() {
        val longTitle = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vel velit venenatis, elementum elit vitae, maximus ligula."
        val post = Post(longTitle, "Description", "Type", 100, 50)

        val result = validatePost(post)

        assertEquals(false, result["title"])
        assertEquals(true, result["description"])
    }

    @Test
    fun testValidatePostWithLongDescription() {
        val longDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vel velit venenatis, elementum elit vitae, maximus ligula."
        val post = Post("Title", longDescription, "Type", 100, 50)

        val result = validatePost(post)

        assertEquals(true, result["title"])
        assertEquals(false, result["description"])
    }
}