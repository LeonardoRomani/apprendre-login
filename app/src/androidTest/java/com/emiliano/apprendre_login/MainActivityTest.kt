package com.emiliano.apprendre_login

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Tests d'instrumentació bàsics per a MainActivity.
 * Comencen amb tests simples per verificar que la configuració funciona.
 */
@RunWith(AndroidJUnit4::class)
class MainActivityBasicTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    /**
     * Test bàsic que verifica que l'Activity s'inicia correctament.
     */
    @Test
    fun testActivityEsLlançadaCorrectament() {
        // Aquest test simplement verifica que l'Activity es llança sense errors
        // La regla createAndroidComposeRule ja s'encarrega de llançar l'Activity
    }

    /**
     * Test que verifica que la pantalla de login es mostra.
     */
    @Test
    fun testPantallaLoginEsVisible() {
        // Busca el text "Inicia sessió" que hauria d'estar a la pantalla de login
        composeTestRule.onNodeWithText("Inicia sessió").assertExists()
    }

    /**
     * Test que verifica que el botó de login està present.
     */
    @Test
    fun testBotoLoginEstaPresent() {
        composeTestRule.onNodeWithText("Login").assertExists()
    }
}