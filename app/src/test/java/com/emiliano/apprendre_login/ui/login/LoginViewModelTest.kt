package com.emiliano.apprendre_login.ui.login

import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        viewModel = LoginViewModel()
    }

    @Test
    fun `login exitós actualitza userRole i loginState`() = runTest {
        // Aquí simulem un login amb credencials correctes
        viewModel.loginState.test {
            viewModel.login("admin", "1234")

            // Obtenim l'event de resultat
            val result = awaitItem()
            assert(result!!.isSuccess)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `login fallit actualitza loginState amb error`() = runTest {
        viewModel.loginState.test {
            viewModel.login("wrong", "wrong")

            val result = awaitItem()
            assert(result!!.isFailure)
            cancelAndIgnoreRemainingEvents()
        }
    }
}