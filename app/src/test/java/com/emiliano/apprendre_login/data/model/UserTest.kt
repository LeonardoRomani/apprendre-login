package com.emiliano.apprendre_login.data.model

import org.junit.Assert.*
import org.junit.Test

/**
 * Tests unitaris per a la classe [User].
 *
 * Comprova la creació correcta d'usuaris, la gestió de valors opcionals,
 * la distinció de rols (1–4), i el comportament dels mètodes equals/hashCode.
 */
class UserTest {

    /**
     * Comprova que un usuari amb totes les dades es crea correctament.
     */
    @Test
    fun testUserCreationWithAllFields() {
        val user = User(
            user_id = 1,
            username = "jdoe",
            name = "John",
            last_name = "Doe",
            email = "jdoe@example.com",
            phone = "666555444",
            role_id = 2,
            last_login = "2025-10-18T14:23:00Z"
        )

        assertEquals(1, user.user_id)
        assertEquals("jdoe", user.username)
        assertEquals("John", user.name)
        assertEquals("Doe", user.last_name)
        assertEquals("jdoe@example.com", user.email)
        assertEquals("666555444", user.phone)
        assertEquals(2, user.role_id)
        assertEquals("2025-10-18T14:23:00Z", user.last_login)
    }

    /**
     * Comprova que un usuari pot tenir camps opcionals nuls sense errors.
     */
    @Test
    fun testUserWithOptionalFieldsNull() {
        val user = User(
            user_id = 2,
            username = "anonymous",
            name = "Anon",
            last_name = "Ymous",
            email = null,
            phone = null,
            role_id = 3,
            last_login = null
        )

        assertNull(user.email)
        assertNull(user.phone)
        assertNull(user.last_login)
        assertEquals(3, user.role_id)
    }

    /**
     * Comprova que els diferents rols d’usuari són vàlids (1–4).
     */
    @Test
    fun testDifferentRoleIds() {
        val admin = User(1, "admin1", "Anna", "Admin", "admin@ex.com", "600111222", 1, null)
        val teacher = User(2, "teacher1", "Tom", "Teacher", "tom@ex.com", "600111223", 2, null)
        val student = User(3, "student1", "Sara", "Student", "sara@ex.com", "600111224", 3, null)
        val parent = User(4, "parent1", "Paul", "Parent", "paul@ex.com", "600111225", 4, null)

        assertEquals(1, admin.role_id)
        assertEquals(2, teacher.role_id)
        assertEquals(3, student.role_id)
        assertEquals(4, parent.role_id) // ✅ Nou rol "Pare"
    }

    /**
     * Comprova que equals() i hashCode() funcionen correctament.
     */
    @Test
    fun testEquality() {
        val user1 = User(5, "alice", "Alice", "Wonder", "alice@ex.com", "600333444", 3, null)
        val user2 = User(5, "alice", "Alice", "Wonder", "alice@ex.com", "600333444", 3, null)

        assertEquals(user1, user2)
        assertEquals(user1.hashCode(), user2.hashCode())
    }

    /**
     * Comprova que dos usuaris diferents no són iguals.
     */
    @Test
    fun testInequality() {
        val user1 = User(6, "bob", "Bob", "Builder", "bob@ex.com", "600444555", 2, null)
        val user2 = User(7, "bobby", "Bobby", "Smith", "bobby@ex.com", "600444556", 3, null)

        assertNotEquals(user1, user2)
    }

    /**
     * Comprova que el número de telèfon pot ser nul o una cadena vàlida.
     */
    @Test
    fun testPhoneCanBeNullOrString() {
        val userWithPhone = User(8, "phoney", "Phil", "Phone", "phil@ex.com", "612345678", 1, null)
        val userWithoutPhone = User(9, "nophone", "Nina", "Nope", "nina@ex.com", null, 2, null)

        assertEquals("612345678", userWithPhone.phone)
        assertNull(userWithoutPhone.phone)
    }
}
