package com.emiliano.apprendre_login.ui.admin

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.emiliano.apprendre_login.data.model.User

@Composable
fun UserForm(
    user: User?,
    onSave: (
        userId: Int,
        username: String,
        name: String,
        lastName: String,
        email: String,
        phone: String,
        roleId: Int,
        password: String
    ) -> Unit
) {
    val context = LocalContext.current
    if (user == null) return

    var username by remember { mutableStateOf(user.username) }
    var email by remember { mutableStateOf(user.email ?: "") }
    var name by remember { mutableStateOf(user.name) }
    var lastName by remember { mutableStateOf(user.last_name) }
    var password by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf(user.phone?.toString() ?: "") }
    var selectedRole by remember { mutableStateOf(user.role_id ?: 3) } // Alumne per defecte si null

    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = username,
            onValueChange = {},
            label = { Text("Nom d'usuari") },
            modifier = Modifier.fillMaxWidth(),
            enabled = false
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correu electrònic") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nom") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Cognom") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Telèfon") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(Modifier.height(8.dp))

        Text("Selecciona rol:")
        Box {
            OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
                Text(
                    when (selectedRole) {
                        1 -> "Administrador"
                        2 -> "Professor"
                        3 -> "Alumne"
                        4 -> "Parent"
                        else -> "Alumne"
                    }
                )
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                DropdownMenuItem(text = { Text("Administrador") }, onClick = { selectedRole = 1; expanded = false })
                DropdownMenuItem(text = { Text("Professor") }, onClick = { selectedRole = 2; expanded = false })
                DropdownMenuItem(text = { Text("Alumne") }, onClick = { selectedRole = 3; expanded = false })
                DropdownMenuItem(text = { Text("Parent") }, onClick = { selectedRole = 4; expanded = false })
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                onSave(
                    user.user_id ?: 0, // Valor per defecte 0 si és null
                    username,
                    name,
                    lastName,
                    email,
                    phone,
                    selectedRole,
                    password
                )
                Toast.makeText(context, "Desant canvis...", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("💾 Desar canvis")
        }
    }
}

