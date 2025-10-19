package com.emiliano.apprendre_login.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import android.widget.Toast
import androidx.compose.ui.platform.testTag
import com.emiliano.apprendre_login.data.model.RegisterRequest
import androidx.navigation.NavController
import com.google.accompanist.flowlayout.FlowRow

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = viewModel(),
    navController: NavController
) {
    // Context i gestor de focus
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    // Estat per cada camp
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var dni by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf(1) }

    // Estats de validació
    var usernameValid by remember { mutableStateOf<Boolean?>(null) }
    var emailValid by remember { mutableStateOf<Boolean?>(null) }
    var nameValid by remember { mutableStateOf<Boolean?>(null) }
    var lastNameValid by remember { mutableStateOf<Boolean?>(null) }
    var dniValid by remember { mutableStateOf<Boolean?>(null) }
    var passwordValid by remember { mutableStateOf<Boolean?>(null) }
    var phoneValid by remember { mutableStateOf<Boolean?>(null) }

    // Estat de resposta del registre (del ViewModel)
    val registerState by viewModel.registerState.collectAsState()

    // Efecte per observar el registre i mostrar missatge / tornar al login
    LaunchedEffect(registerState) {
        registerState?.let { response ->
            if (response.contains("exitosament", ignoreCase = true) ||
                response.contains("creat", ignoreCase = true)) {
                Toast.makeText(context, "Registre exitós", Toast.LENGTH_LONG).show()
                kotlinx.coroutines.delay(2000)
                navController.navigate("login") {
                    popUpTo("register") { inclusive = true }
                }
            } else {
                Toast.makeText(context, response, Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Funcions de validació per cada camp
    fun validateUsername(value: String) = value.matches(Regex("^[a-zA-Z0-9]+$"))
    fun validateEmail(value: String) = value.matches(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$"))
    fun validateName(value: String) = value.matches(Regex("^[a-zA-Z]+$"))
    fun validateDni(value: String) = value.matches(Regex("^\\d{8}[A-Za-z]\$"))
    fun validatePassword(value: String) = value.isNotEmpty()
    fun validatePhone(value: String) = value.matches(Regex("^[0-9+ ]{9,15}\$"))

    // Camp de text amb validació i testTag
    @Composable
    fun TextFieldWithValidation(
        value: String,
        onValueChange: (String) -> Unit,
        label: String,
        valid: Boolean? = null,
        keyboardType: KeyboardType = KeyboardType.Text,
        onValidate: (String) -> Boolean
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            label = { Text(label) },
            isError = valid == false,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(label),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Down) }
            )
        )
    }

    // Layout principal amb scroll vertical
    val scrollState = rememberScrollState()
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            // Títol
            Text("Crear nou usuari", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(16.dp))

            // Camps de registre
            TextFieldWithValidation(username, { username = it; usernameValid = validateUsername(it) }, "Usuari", usernameValid, onValidate = ::validateUsername)
            TextFieldWithValidation(email, { email = it; emailValid = validateEmail(it) }, "Email", emailValid, keyboardType = KeyboardType.Email, onValidate = ::validateEmail)
            TextFieldWithValidation(name, { name = it; nameValid = validateName(it) }, "Nom", nameValid, onValidate = ::validateName)
            TextFieldWithValidation(lastName, { lastName = it; lastNameValid = validateName(it) }, "Cognom", lastNameValid, onValidate = ::validateName)
            TextFieldWithValidation(dni, { dni = it; dniValid = validateDni(it) }, "DNI", dniValid, onValidate = ::validateDni)
            TextFieldWithValidation(password, { password = it; passwordValid = validatePassword(it) }, "Contrasenya", passwordValid, keyboardType = KeyboardType.Password, onValidate = ::validatePassword)
            TextFieldWithValidation(phone, { phone = it; phoneValid = validatePhone(it) }, "Telèfon", phoneValid, keyboardType = KeyboardType.Number, onValidate = ::validatePhone)

            Spacer(Modifier.height(16.dp))

            // Selecció de rol amb FlowRow
            Text("Selecciona rol:")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                val rolesMap = mapOf(
                    1 to "Administrador",
                    2 to "Professor",
                    3 to "Alumne",
                    4 to "Tutor"
                )
                rolesMap.forEach { (id, nomRol) ->
                    Button(
                        onClick = { selectedRole = id },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedRole == id) Color.Green else MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("$id - $nomRol")
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Botó per registrar
            Button(
                onClick = {
                    // Validació
                    usernameValid = validateUsername(username)
                    emailValid = validateEmail(email)
                    nameValid = validateName(name)
                    lastNameValid = validateName(lastName)
                    dniValid = validateDni(dni)
                    passwordValid = validatePassword(password)
                    phoneValid = validatePhone(phone)

                    if (listOf(usernameValid, emailValid, nameValid, lastNameValid, dniValid, passwordValid, phoneValid).all { it == true }) {
                        val request = RegisterRequest(
                            username = username,
                            email = email,
                            name = name,
                            last_name = lastName,
                            dni = dni,
                            password = password,
                            phone = phone,
                            role_id = selectedRole
                        )
                        viewModel.register(request)
                    } else {
                        Toast.makeText(context, "Corregeix els camps en vermell", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar")
            }

            Spacer(Modifier.height(8.dp))

            // Botó per tornar al login
            OutlinedButton(
                onClick = { navController.navigate("login") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tornar al Login")
            }
        }
    }
}