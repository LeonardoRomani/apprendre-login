package com.emiliano.apprendre_login.network

import com.emiliano.apprendre_login.api.ApiService
import com.emiliano.apprendre_login.data.model.LoginRequest
import com.emiliano.apprendre_login.data.model.LoginResponse
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiServiceTest {

    // Declaració de variables per al servidor simulat i el servei API
    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: ApiService

    @Before
    fun setUp() {
        // Inicialitzem MockWebServer abans de cada test
        mockWebServer = MockWebServer()
        mockWebServer.start() // Iniciem el servidor local

        // Configurem Retrofit perquè apunti al MockWebServer
        // Això ens permet interceptar les crides HTTP i retornar respostes simulades
        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/")) // Base URL apuntant al servidor simulat
            .addConverterFactory(GsonConverterFactory.create()) // Gson per deserialitzar JSON
            .build()
            .create(ApiService::class.java)
    }

    @After
    fun tearDown() {
        // Tanquem el MockWebServer després de cada test per alliberar recursos
        mockWebServer.shutdown()
    }

    @Test
    fun testLoginRequestSuccess() {
        // runBlocking permet executar codi suspensiu dins d’un test JUnit 4
        runBlocking {

            // Configuració de la resposta simulada que MockWebServer retornarà
            // Aquesta resposta imita el JSON que vindria del servidor real
            val mockResponse = MockResponse()
                .setResponseCode(200) // Codi HTTP de la resposta (200 = èxit)
                .setBody("""
                {
                    "access_token": "mock_token",
                    "token_type": "bearer",
                    "user_id": "1",
                    "username": "testuser",
                    "role": 2,
                    "errorMessage": null
                }
            """.trimIndent()) // Cos de la resposta en format JSON
            mockWebServer.enqueue(mockResponse) // Afegim la resposta a la cua del servidor

            // Creem l’objecte LoginRequest amb les credencials a provar
            val request = LoginRequest(username = "testuser", password = "123456")

            // Fem la crida a l'API (aquí MockWebServer interceptarà la petició)
            val response = apiService.login(request)

            // Verifiquem que la crida HTTP ha tingut èxit (codi 2xx)
            assertTrue(response.isSuccessful)

            // Obtenim el cos de la resposta deserialitzat a LoginResponse
            val body: LoginResponse? = response.body()

            // Comprovem que el cos no sigui null
            assertNotNull(body)

            // Comprovem que els camps del JSON s'han deserialitzat correctament
            body?.let {
                assertEquals("mock_token", it.access_token) // Token d’accés
                assertEquals("bearer", it.token_type)       // Tipus de token
                assertEquals("testuser", it.username)      // Nom d’usuari
                assertEquals(2, it.role)                   // Rol d’usuari
                assertNull(it.errorMessage)                // No hi ha errors
            }
        }
    }
}