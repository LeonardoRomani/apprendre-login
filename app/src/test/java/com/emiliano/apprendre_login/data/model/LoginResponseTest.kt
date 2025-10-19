package com.emiliano.apprendre_login.data.model

import org.junit.Assert
import org.junit.Test

class LoginResponseTest {

    @Test
    fun testLoginResponseSuccess() {
        val response = LoginResponse(
            access_token = "abc123",
            token_type = "bearer",
            user_id = "42",
            username = "admin",
            role = 1,
            errorMessage = null
        )

        Assert.assertEquals("abc123", response.access_token)
        Assert.assertEquals("bearer", response.token_type)
        Assert.assertEquals("42", response.user_id)
        Assert.assertEquals("admin", response.username)
        Assert.assertEquals(1, response.role)
        Assert.assertNull(response.errorMessage)
    }

    @Test
    fun testLoginResponseError() {
        val response = LoginResponse(
            access_token = null,
            token_type = null,
            user_id = null,
            username = null,
            role = null,
            errorMessage = "Invalid credentials"
        )

        Assert.assertNull(response.access_token)
        Assert.assertEquals("Invalid credentials", response.errorMessage)
    }
}
