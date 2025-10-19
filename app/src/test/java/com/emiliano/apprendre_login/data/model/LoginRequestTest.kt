package com.emiliano.apprendre_login.data.model

import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Test

/**
 * Tests unitaris per a la classe [LoginRequest].
 * Comprova que la instància es crea correctament i que la serialització JSON funciona.
 */
class LoginRequestTest {

    private val gson = Gson()

    @Test
    fun `creacio basica de LoginRequest`() {
        val login = LoginRequest(
            username = "johndoe",
            password = "1234"
        )

        // Comprovem que els camps contenen els valors correctes
        assertEquals("johndoe", login.username)
        assertEquals("1234", login.password)
    }

    @Test
    fun `serialitzacio JSON correcta`() {
        val login = LoginRequest(
            username = "janedoe",
            password = "abcd"
        )

        val json = gson.toJson(login)
        println("JSON generat: $json")

        // Comprovem que els noms i valors dels camps són correctes
        assertTrue(json.contains("\"username\":\"janedoe\""))
        assertTrue(json.contains("\"password\":\"abcd\""))
    }
}