package com.emiliano.apprendre_login.ui.login

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.emiliano.apprendre_login.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun loginScreen_displaysInputsAndButtons() {
        // Comprovem que els elements existeixen sobre MainActivity real
        composeRule.onNodeWithText("Usuari").assertExists()
        composeRule.onNodeWithText("Contrasenya").assertExists()
        composeRule.onNodeWithText("Login").assertExists()
        composeRule.onNodeWithText("¿No tens compte? Enregistra’t aquí").assertExists()
    }

    @Test
    fun loginScreen_inputsAndLoginButton_canBeUsed() {
        // Omplim els TextField
        composeRule.onNodeWithText("Usuari").performTextInput("testuser")
        composeRule.onNodeWithText("Contrasenya").performTextInput("123456")

        // Cliquem el botó de Login
        composeRule.onNodeWithText("Login").performClick()

        // Aquí no podem comprovar directament la crida a login() del ViewModel real
        // però podem verificar algun efecte visual, per exemple que aparegui un missatge d'error o loading
        // Ex: composeRule.onNodeWithText("Error: usuari incorrecte").assertIsDisplayed()
    }

    @Test
    fun loginScreen_navigationElementsExist() {
        // Comprovem que els callbacks de navegació estan presents visualment
        composeRule.onNodeWithText("¿No tens compte? Enregistra’t aquí").assertExists()
    }
}