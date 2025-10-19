package com.emiliano.apprendre_login.ui.login

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import org.junit.Rule
import org.junit.Test

class RegisterScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun registerScreen_displaysAllFieldsAndButtons() {
        // Carreguem la pantalla de registre
        composeTestRule.setContent {
            RegisterScreen(
                navController = rememberNavController()
            )
        }

        // Comprovem que existeixen tots els camps amb els testTags
        composeTestRule.onNodeWithTag("Usuari").assertExists()
        composeTestRule.onNodeWithTag("Email").assertExists()
        composeTestRule.onNodeWithTag("Nom").assertExists()
        composeTestRule.onNodeWithTag("Cognom").assertExists()
        composeTestRule.onNodeWithTag("DNI").assertExists()
        composeTestRule.onNodeWithTag("Contrasenya").assertExists()
        composeTestRule.onNodeWithTag("Telèfon").assertExists()

        // Comprovem que existeixen els botons de rol amb els textos actualitzats
        val roles = listOf(
            "1 - Administrador",
            "2 - Professor",
            "3 - Alumne",
            "4 - Tutor"
        )
        roles.forEach { rolText ->
            composeTestRule.onNodeWithText(rolText).assertExists()
        }

        // Comprovem que existeixen els botons de registrar i tornar al login
        composeTestRule.onNodeWithText("Registrar").assertExists()
        composeTestRule.onNodeWithText("Tornar al Login").assertExists()
    }
}