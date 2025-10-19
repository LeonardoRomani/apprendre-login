package com.emiliano.apprendre_login.data.model

import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Test

/**
 * Tests unitaris per a la classe [RegisterRequest].
 * Comprova que es comporta correctament tant amb camps obligatoris com opcionals.
 */
class RegisterRequestTest {

    private val gson = Gson()

    @Test
    fun `creacio basica de RegisterRequest`() {
        val req = RegisterRequest(
            username = "johndoe",
            email = "john@doe.com",
            name = "John",
            last_name = "Doe",
            dni = "12345678A",
            password = "1234",
            phone = "654321987",
            role_id = 2
        )

        assertEquals("johndoe", req.username)
        assertEquals("john@doe.com", req.email)
        assertEquals("John", req.name)
        assertEquals("Doe", req.last_name)
        assertEquals("12345678A", req.dni)
        assertEquals("1234", req.password)
        assertEquals("654321987", req.phone)
        assertEquals(2, req.role_id)
    }

    @Test
    fun `admetre valors nuls per dni i phone`() {
        val req = RegisterRequest(
            username = "janedoe",
            email = "jane@doe.com",
            name = "Jane",
            last_name = "Doe",
            dni = null,
            password = "abcd",
            phone = null,
            role_id = 3
        )

        assertNull(req.dni)
        assertNull(req.phone)
        assertEquals(3, req.role_id)
    }

    @Test
    fun `serialitzacio JSON correcta`() {
        val req = RegisterRequest(
            username = "user123",
            email = "user@test.com",
            name = "User",
            last_name = "Test",
            dni = "99999999Z",
            password = "secure",
            phone = "123456789",
            role_id = 4
        )

        val json = gson.toJson(req)
        println("JSON generat: $json")

        assertTrue(json.contains("\"username\":\"user123\""))
        assertTrue(json.contains("\"email\":\"user@test.com\""))
        assertTrue(json.contains("\"name\":\"User\""))
        assertTrue(json.contains("\"last_name\":\"Test\""))
        assertTrue(json.contains("\"dni\":\"99999999Z\""))
        assertTrue(json.contains("\"phone\":\"123456789\""))
        assertTrue(json.contains("\"role_id\":4"))
    }

    @Test
    fun `serialitzacio JSON sense camps opcionals`() {
        val req = RegisterRequest(
            username = "nooptionals",
            email = "no@opt.com",
            name = "No",
            last_name = "Optionals",
            dni = null,
            password = "pass",
            phone = null,
            role_id = 1
        )

        val json = gson.toJson(req)
        println("JSON sense opcionals: $json")

        // Els camps opcionals no haurien d'estar presents en el JSON
        assertFalse(json.contains("dni"))
        assertFalse(json.contains("phone"))
        assertTrue(json.contains("\"role_id\":1"))
    }
}
