package com.emiliano.apprendre_login.ui.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.emiliano.apprendre_login.R

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onNavigateToRegister: () -> Unit,
    onNavigateToAdmin: () -> Unit,
    onNavigateToTeacher: () -> Unit,
    onNavigateToStudent: () -> Unit,
    onNavigateToParent: () -> Unit
) {
    val context = LocalContext.current

    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    val loginState by viewModel.loginState.collectAsState()
    val userRole by viewModel.userRole.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(loginState) {
        loginState?.let { result ->
            result.onSuccess { response ->
                Toast.makeText(
                    context,
                    "Benvingut ${response.username ?: "desconegut"}",
                    Toast.LENGTH_SHORT
                ).show()
            }.onFailure { e ->
                Toast.makeText(
                    context,
                    e.message ?: "Error desconegut",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    LaunchedEffect(userRole) {
        when (userRole) {
            1 -> onNavigateToAdmin()
            2 -> onNavigateToTeacher()
            3 -> onNavigateToStudent()
            4 -> onNavigateToParent()
        }
    }

    // Scroll vertical per a pantalles petites
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // ----------------------------
        // Imatge superior
        // ----------------------------
        Image(
            painter = painterResource(id = R.drawable.ioc),
            contentDescription = "Logo superior",
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )

        Text(
            text = "Inicia sessió",
            style = MaterialTheme.typography.headlineSmall
        )

        // ----------------------------
        // Camps de login
        // ----------------------------
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuari") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contrasenya") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),
            trailingIcon = {
                val icon = if (passwordVisible)
                    Icons.Filled.VisibilityOff
                else
                    Icons.Filled.Visibility
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(icon, contentDescription = "Mostrar / Ocultar contrasenya")
                }
            }
        )

        Button(
            onClick = { viewModel.login(username, password) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(20.dp)
                        .padding(end = 8.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text("Iniciant sessió...")
            } else {
                Text("Login")
            }
        }

        TextButton(onClick = onNavigateToRegister) {
            Text("¿No tens compte? Enregistra’t aquí")
        }

        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ----------------------------
        // Imatges inferiors
        // ----------------------------
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(
                painter = painterResource(id = R.drawable.apprendre),
                contentDescription = "Imatge inferior esquerra",
                modifier = Modifier
                    .height(100.dp)
                    .weight(1f)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Image(
                painter = painterResource(id = R.drawable.dev4fun),
                contentDescription = "Imatge inferior dreta",
                modifier = Modifier
                    .height(100.dp)
                    .weight(1f)
            )
        }
    }
}
