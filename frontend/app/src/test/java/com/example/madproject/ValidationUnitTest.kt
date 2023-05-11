package com.example.madproject

import android.util.Log
import com.example.madproject.models.NewGroup
import com.example.madproject.util.Validation
import org.junit.Assert.*
import org.junit.Test

class ValidationUnitTest {

    @Test
    fun testValidateGroup_withEmptyName_returnsValidationError() {
        val group = NewGroup("", "description")
        val result = Validation.validateGroup(group)
        assertFalse(result["name"]?.isValid!!)
        assertEquals("Name cannot be empty", result["name"]?.message)
    }

    @Test
    fun testValidateGroup_withInvalidName_returnsValidationError() {
        val group = NewGroup("invalid /';name!", "description")
        val result = Validation.validateGroup(group)
        assertFalse(result["name"]?.isValid!!)
    }

    @Test
    fun testValidateGroup_withLongDescription_returnsValidationError() {
        val group = NewGroup("valid_name", "a".repeat(151))
        val result = Validation.validateGroup(group)
        assertFalse(result["description"]?.isValid!!)
        assertEquals("Description cannot be longer than 150 characters", result["description"]?.message)
    }

    @Test
    fun testValidateGroup_withShortDescription_returnsValidationSuccess() {
        val group = NewGroup("valid_name", "short description")
        val result = Validation.validateGroup(group)
        assertTrue(result["description"]?.isValid!!)
        assertNull(result["description"]?.message)
    }

    @Test
    fun testValidateGroup_withValidName_returnsValidationSuccess() {
        val group = NewGroup("name1234", "description")
        val result = Validation.validateGroup(group)
        assertTrue(result["name"]?.isValid!!)
        assertNull(result["name"]?.message)
    }
}
