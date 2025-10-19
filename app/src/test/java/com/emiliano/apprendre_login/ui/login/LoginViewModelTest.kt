package com.emiliano.apprendre_login.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.emiliano.apprendre_login.api.ApiService
import com.emiliano.apprendre_login.api.AuthTokenManager
import com.emiliano.apprendre_login.data.model.LoginRequest
import com.emiliano.apprendre_login.data.model.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import org.mockito.kotlin.any
import retrofit2.Response

/**
 * Test unitari per a la classe LoginViewModel.
 * Aquest test comprova el comportament del mètode login() sota diferents condicions:
 *  - Èxit amb resposta vàlida.
 *  - Error de credencials (HTTP 401).
 *  - Error de connexió (Excepció).
 *  - Validació de camps buits.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    // Ens assegurem que LiveData i StateFlow s'executin immediatament
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // Dispatcher per controlar les coroutines en test
    private val testDispatcher = StandardTestDispatcher()

    // Mocks dels components dependents
    @Mock
    private lateinit var mockApiService: ApiService

    private lateinit var loginViewModel: LoginViewModel

    private lateinit var closeable: AutoCloseable

    @Before
    fun setUp() {
        // Inicialitzem Mockito i assignem el dispatcher de proves
        Dispatchers.setMain(testDispatcher)
        closeable = MockitoAnnotations.openMocks(this)

        // Creem el ViewModel real però injectem el mock manualment
        loginViewModel = LoginViewModel()

        // Substituïm el camp privat "apiService" pel mock
        val field = loginViewModel.javaClass.getDeclaredField("apiService")
        field.isAccessible = true
        field.set(loginViewModel, mockApiService)
    }

    @After
    fun tearDown() {
        // Alliberem recursos de Mockito i restablim el dispatcher principal
        closeable.close()
        Dispatchers.resetMain()
    }

    // -------------------- TEST 1 --------------------
    @Test
    fun `login amb credencials correctes retorna èxit`() = runTest {
        // Simulem una resposta de l'API amb èxit
        val mockResponse = LoginResponse(
            access_token = "mock_token",
            token_type = "bearer",
            user_id = 1.toString(),
            username = "testuser",
            role = 2,
            errorMessage = null
        )

        // Configurem el mock perquè retorni aquesta resposta
        whenever(mockApiService.login(any())).thenReturn(Response.success(mockResponse))

        // Executem el mètode login
        loginViewModel.login("testuser", "1234")

        // Avancem el temps de les coroutines
        testDispatcher.scheduler.advanceUntilIdle()

        // Comprovem que el login ha estat exitós
        val state = loginViewModel.loginState.value
        Assert.assertTrue(state?.isSuccess == true)

        val result = state?.getOrNull()
        Assert.assertEquals("testuser", result?.username)
        Assert.assertEquals(2, result?.role)
    }

    // -------------------- TEST 2 --------------------
    @Test
    fun `login amb credencials incorrectes retorna error 401`() = runTest {
        // Simulem una resposta 401 (Unauthorized)
        val errorResponse = Response.error<LoginResponse>(
            401,
            okhttp3.ResponseBody.create(null, "")
        )
        whenever(mockApiService.login(any())).thenReturn(errorResponse)

        loginViewModel.login("wronguser", "wrongpass")
        testDispatcher.scheduler.advanceUntilIdle()

        // Comprovem que s'ha generat el missatge d'error correcte
        val error = loginViewModel.errorMessage.value
        Assert.assertEquals("Credencials incorrectes", error)
    }

    // -------------------- TEST 3 --------------------
    @Test
    fun `login amb error de xarxa genera missatge d'error`() = runTest {
        // Simulem una excepció de connexió
        whenever(mockApiService.login(any())).thenThrow(RuntimeException("Network error"))

        loginViewModel.login("testuser", "1234")
        testDispatcher.scheduler.advanceUntilIdle()

        val error = loginViewModel.errorMessage.value
        Assert.assertTrue(error?.contains("Error de connexió") == true)
    }

    // -------------------- TEST 4 --------------------
    @Test
    fun `login amb camps buits mostra missatge de validació`() = runTest {
        // No cal simular res perquè el ViewModel gestiona això internament
        loginViewModel.login("", "")
        testDispatcher.scheduler.advanceUntilIdle()

        val error = loginViewModel.errorMessage.value
        Assert.assertEquals("Si us plau, omple tots els camps", error)
    }

    // -------------------- TEST 5 --------------------
    @Test
    fun `clearError neteja el missatge d'error`() = runTest {
        loginViewModel.login("", "")
        testDispatcher.scheduler.advanceUntilIdle()

        Assert.assertNotNull(loginViewModel.errorMessage.value)

        loginViewModel.clearError()
        Assert.assertNull(loginViewModel.errorMessage.value)
    }

    // -------------------- TEST 6 --------------------
    @Test
    fun `clearLoginState reinicia l'estat del login`() = runTest {
        // Preparem una resposta exitosa
        val mockResponse = LoginResponse(
            access_token = "mock_token",
            token_type = "bearer",
            user_id = 1.toString(),
            username = "testuser",
            role = 2,
            errorMessage = null
        )
        whenever(mockApiService.login(any())).thenReturn(Response.success(mockResponse))

        loginViewModel.login("testuser", "1234")
        testDispatcher.scheduler.advanceUntilIdle()

        Assert.assertNotNull(loginViewModel.loginState.value)

        loginViewModel.clearLoginState()
        Assert.assertNull(loginViewModel.loginState.value)
    }
}