package com.emiliano.apprendre_login.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.emiliano.apprendre_login.api.ApiService
import com.emiliano.apprendre_login.data.model.RegisterRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody
import org.junit.*
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import retrofit2.Response

/**
 * Test unitari per a la classe RegisterViewModel.
 *
 * Comprova el comportament del mètode register() sota diferents condicions:
 *  - Èxit de registre.
 *  - Error HTTP del servidor.
 *  - Error de connexió (excepció de xarxa).
 */
@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelTest {

    // Assegura que les coroutines i StateFlow s'executin de manera síncrona
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var mockApiService: ApiService

    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var closeable: AutoCloseable

    @Before
    fun setUp() {
        // Inicialitzem Mockito i establim el dispatcher de proves
        Dispatchers.setMain(testDispatcher)
        closeable = MockitoAnnotations.openMocks(this)

        // Instància real del ViewModel
        registerViewModel = RegisterViewModel()

        // Substituïm el camp privat apiService pel mock
        val field = registerViewModel.javaClass.getDeclaredField("apiService")
        field.isAccessible = true
        field.set(registerViewModel, mockApiService)
    }

    @After
    fun tearDown() {
        // Alliberem recursos
        closeable.close()
        Dispatchers.resetMain()
    }

    // ------------------------ TEST 1 ------------------------
    @Test
    fun `register amb èxit retorna missatge de confirmació`() = runTest {
        // Creem una petició de registre simulada
        val request = RegisterRequest(
            username = "testuser",
            email = "test@example.com",
            name = "John",
            last_name = "Doe",
            dni = "12345678A",
            password = "password123",
            phone = "600123456",
            role_id = 3
        )

        // Simulem una resposta amb èxit
        whenever(mockApiService.register(any())).thenReturn(Response.success("Usuari creat"))

        // Executem el mètode
        registerViewModel.register(request)
        testDispatcher.scheduler.advanceUntilIdle()

        // Verifiquem el resultat
        Assert.assertEquals("Usuari creat exitosamente", registerViewModel.registerState.value)
    }

    // ------------------------ TEST 2 ------------------------
    @Test
    fun `register amb error HTTP retorna missatge d'error`() = runTest {
        val request = RegisterRequest(
            username = "testuser",
            email = "test@example.com",
            name = "John",
            last_name = "Doe",
            dni = "12345678A",
            password = "password123",
            phone = "600123456",
            role_id = 3
        )

        // Simulem una resposta d'error HTTP 400
        val errorResponse = Response.error<String>(
            400,
            ResponseBody.create(null, "")
        )
        whenever(mockApiService.register(any())).thenReturn(errorResponse)

        registerViewModel.register(request)
        testDispatcher.scheduler.advanceUntilIdle()

        // Verifiquem el missatge d'error
        Assert.assertEquals("Error al registrar usuari: 400", registerViewModel.registerState.value)
    }

    // ------------------------ TEST 3 ------------------------
    @Test
    fun `register amb error de connexió retorna missatge d'error de xarxa`() = runTest {
        val request = RegisterRequest(
            username = "testuser",
            email = "test@example.com",
            name = "John",
            last_name = "Doe",
            dni = "12345678A",
            password = "password123",
            phone = "600123456",
            role_id = 3
        )

        // Simulem una excepció
        whenever(mockApiService.register(any())).thenThrow(RuntimeException("Network failure"))

        registerViewModel.register(request)
        testDispatcher.scheduler.advanceUntilIdle()

        // Verifiquem que el missatge d'error contingui la paraula "Error de connexió"
        val result = registerViewModel.registerState.value
        Assert.assertTrue(result?.contains("Error de connexió") == true)
    }
}