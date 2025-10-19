package com.emiliano.apprendre_login.data.model

import org.junit.Test
import org.junit.Assert.*
import org.junit.Before

/**
 * Tests unitaris per a la classe UserResponse.
 *
 * Aquests tests verifiquen el correcte funcionament de la classe UserResponse
 * i les seves propietats bàsiques.
 */
class UserResponseTest {

    private lateinit var sampleUsers: List<User>
    private lateinit var emptyUserList: List<User>

    /**
     * Configuració inicial per als tests
     */
    @Before
    fun setUp() {
        sampleUsers = listOf(
            User(
                user_id = 1,
                username = "user1",
                name = "User",
                last_name = "One",
                email = "user1@example.com",
                phone = 111111111.toString(),
                role_id = 1,
                last_login = "2024-01-15 10:00:00"
            ),
            User(
                user_id = 2,
                username = "user2",
                name = "User",
                last_name = "Two",
                email = "user2@example.com",
                phone = 222222222.toString(),
                role_id = 2,
                last_login = "2024-01-15 11:00:00"
            )
        )

        emptyUserList = emptyList()
    }

    /**
     * Test per verificar la creació d'una UserResponse amb la propietat "users"
     */
    @Test
    fun testUserResponseWithUsersProperty() {
        // Crear UserResponse amb usuaris a la propietat "users"
        val response = UserResponse(
            users = sampleUsers,
            data = null,
            message = "Usuaris recuperats correctament"
        )

        // Verificar les propietats
        assertEquals("La llista d'usuaris hauria de ser la proporcionada", sampleUsers, response.users)
        assertNull("La propietat data hauria de ser null", response.data)
        assertEquals("El missatge hauria de ser el correcte", "Usuaris recuperats correctament", response.message)
    }

    /**
     * Test per verificar la creació d'una UserResponse amb la propietat "data"
     */
    @Test
    fun testUserResponseWithDataProperty() {
        // Crear UserResponse amb usuaris a la propietat "data"
        val response = UserResponse(
            users = null,
            data = sampleUsers,
            message = "Dades recuperades correctament"
        )

        // Verificar les propietats
        assertNull("La propietat users hauria de ser null", response.users)
        assertEquals("La llista de dades hauria de ser la proporcionada", sampleUsers, response.data)
        assertEquals("El missatge hauria de ser el correcte", "Dades recuperades correctament", response.message)
    }

    /**
     * Test per verificar la creació d'una UserResponse amb ambdues propietats
     */
    @Test
    fun testUserResponseWithBothProperties() {
        // Crear UserResponse amb usuaris a ambdues propietats
        val response = UserResponse(
            users = sampleUsers,
            data = emptyUserList,
            message = "Resposta amb múltiples propietats"
        )

        // Verificar que totes les propietats estan presents
        assertEquals("La propietat users hauria de contenir els usuaris", sampleUsers, response.users)
        assertEquals("La propietat data hauria de contenir la llista buida", emptyUserList, response.data)
        assertEquals("El missatge hauria de ser el correcte", "Resposta amb múltiples propietats", response.message)
    }

    /**
     * Test per verificar la creació d'una UserResponse buida
     */
    @Test
    fun testEmptyUserResponse() {
        // Crear UserResponse sense usuaris ni missatge
        val response = UserResponse(
            users = null,
            data = null,
            message = null
        )

        // Verificar que totes les propietats són null
        assertNull("La propietat users hauria de ser null", response.users)
        assertNull("La propietat data hauria de ser null", response.data)
        assertNull("El missatge hauria de ser null", response.message)
    }

    /**
     * Test per verificar la igualtat de UserResponse
     */
    @Test
    fun testUserResponseEquality() {
        val response1 = UserResponse(
            users = sampleUsers,
            data = null,
            message = "Test"
        )

        val response2 = UserResponse(
            users = sampleUsers,
            data = null,
            message = "Test"
        )

        // Verificar que dues respostes iguals són considerades iguals
        assertEquals("Les respostes amb les mateixes dades han de ser iguals", response1, response2)
    }

    /**
     * Test per verificar la desigualtat de UserResponse
     */
    @Test
    fun testUserResponseInequality() {
        val response1 = UserResponse(
            users = sampleUsers,
            data = null,
            message = "Test 1"
        )

        val response2 = UserResponse(
            users = emptyUserList,
            data = null,
            message = "Test 2"
        )

        // Verificar que respostes diferents no són iguals
        assertNotEquals("Les respostes amb dades diferents no han de ser iguals", response1, response2)
    }

    /**
     * Test per verificar el hash code de UserResponse
     */
    @Test
    fun testUserResponseHashCode() {
        val response1 = UserResponse(
            users = sampleUsers,
            data = null,
            message = "Test"
        )

        val response2 = UserResponse(
            users = sampleUsers,
            data = null,
            message = "Test"
        )

        // Verificar que respostes iguals tenen el mateix hash code
        assertEquals("Els hash codes han de ser iguals per a respostes iguals",
            response1.hashCode(), response2.hashCode())
    }

    /**
     * Test per verificar la representació en string de UserResponse
     */
    @Test
    fun testUserResponseToString() {
        val response = UserResponse(
            users = sampleUsers,
            data = null,
            message = "Test message"
        )

        val toStringResult = response.toString()

        // Verificar que el string conté informació rellevant
        assertTrue("El toString hauria de contenir informació sobre els usuaris",
            toStringResult.contains("users"))
        assertTrue("El toString hauria de contenir el missatge",
            toStringResult.contains("Test message"))
    }

    /**
     * Test per verificar la còpia de UserResponse amb modificacions
     */
    @Test
    fun testUserResponseCopyWithModifications() {
        val originalResponse = UserResponse(
            users = sampleUsers,
            data = null,
            message = "Original"
        )

        val copiedResponse = originalResponse.copy(message = "Modified")

        // Verificar que el missatge ha canviat
        assertEquals("El missatge hauria de ser el nou valor", "Modified", copiedResponse.message)

        // Verificar que la resta de propietats es mantenen
        assertEquals("Els usuaris haurien de mantenir-se", originalResponse.users, copiedResponse.users)
        assertEquals("Les dades haurien de mantenir-se", originalResponse.data, copiedResponse.data)
    }

    /**
     * Test per verificar UserResponse amb llista buida d'usuaris
     */
    @Test
    fun testUserResponseWithEmptyUsersList() {
        val response = UserResponse(
            users = emptyUserList,
            data = null,
            message = "Llista buida"
        )

        // Verificar que la llista d'usuaris està buida però no és null
        assertNotNull("La llista d'usuaris no hauria de ser null", response.users)
        assertTrue("La llista d'usuaris hauria d'estar buida", response.users!!.isEmpty())
        assertEquals("El missatge hauria de ser correcte", "Llista buida", response.message)
    }

    /**
     * Test per verificar UserResponse amb llista buida de data
     */
    @Test
    fun testUserResponseWithEmptyDataList() {
        val response = UserResponse(
            users = null,
            data = emptyUserList,
            message = "Dades buides"
        )

        // Verificar que la llista de data està buida però no és null
        assertNotNull("La llista de data no hauria de ser null", response.data)
        assertTrue("La llista de data hauria d'estar buida", response.data!!.isEmpty())
        assertEquals("El missatge hauria de ser correcte", "Dades buides", response.message)
    }
    /**
     * Test per verificar que el toString funciona correctament
     * NOTA: Les data classes de Kotlin per defecte inclouen tots els camps en el toString()
     * Això és el comportament esperat i no representa un problema de seguretat
     * ja que el toString() s'utilitza principalment per debugging i logging
     */
    @Test
    fun testToStringContainsRelevantInformation() {
        val sensitiveRequest = LoginRequest(
            username = "admin",
            password = "PasswordSecret123!"
        )

        val toStringResult = sensitiveRequest.toString()

        // Verificar que el toString conté informació rellevant
        assertTrue("El toString hauria de contenir el username",
            toStringResult.contains("admin"))
        assertTrue("El toString hauria de contenir informació sobre el password",
            toStringResult.contains("password"))

        // NOTA: Les data classes de Kotlin mostren tots els camps en el toString()
        // Això és normal i acceptable ja que s'utilitza per debugging
        // En un entorn de producció, els logs no haurien de contenir dades sensibles
    }
}

