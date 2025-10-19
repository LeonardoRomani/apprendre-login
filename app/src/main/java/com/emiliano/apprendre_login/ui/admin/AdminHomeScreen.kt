package com.emiliano.apprendre_login.ui.admin

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.emiliano.apprendre_login.data.model.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminHomeScreen(
    viewModel: AdminViewModel = viewModel(),
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val users by viewModel.usersState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.errorMessage.collectAsState()
    val logoutState by viewModel.logoutState.collectAsState()
    val roleFilter by viewModel.roleFilter.collectAsState()
    val currentUserDialog by viewModel.currentUserDialog.collectAsState()
    val userEditDialog by viewModel.userEditDialog.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    LaunchedEffect(logoutState) { if (logoutState) onLogout() }
    LaunchedEffect(error) { error?.let { Toast.makeText(context, it, Toast.LENGTH_LONG).show() } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { currentUser?.let { viewModel.showCurrentUserDialog(it) } }) {
                            Icon(Icons.Default.AccountCircle, contentDescription = "Usuari")
                        }
                        Spacer(Modifier.width(8.dp))
                        Text(text = currentUser?.name ?: currentUser?.username ?: "Usuari desconegut")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.logout { } }) {
                        Icon(Icons.Default.Logout, contentDescription = "Tancar sessió")
                    }
                }
            )
        },
        content = { padding ->
            Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                        RoleDropdown(selectedRole = roleFilter, onSelect = { viewModel.setRoleFilter(it) })
                        Spacer(Modifier.height(16.dp))

                        val filtered = viewModel.filteredUsers()
                        if (filtered.isEmpty()) Text("No s'han trobat usuaris")
                        else LazyColumn {
                            items(filtered) { user ->
                                UserRow(user = user,
                                    onEdit = { viewModel.showUserEditDialog(user) },
                                    onDelete = {
                                        viewModel.deleteUser(user.user_id) {
                                            Toast.makeText(context, "Usuari eliminat", Toast.LENGTH_SHORT).show()
                                        }
                                    })
                            }
                        }
                    }
                }
            }
        }
    )

    // Diàleg per editar usuari des del llistat
    userEditDialog?.let { u ->
        AlertDialog(
            onDismissRequest = { viewModel.dismissUserEditDialog() },
            title = { Text("Editar usuari: ${u.username}") },
            text = {
                UserForm(user = u) { id, username, name, lastName, email, phone, roleId, password ->
                    viewModel.updateUser(
                        userId = id,
                        username = username,
                        name = name,
                        lastName = lastName,
                        email = email,
                        phone = phone,
                        roleId = roleId,
                        password = password,
                        onSuccess = {
                            viewModel.dismissUserEditDialog()
                            Toast.makeText(context, "Usuari actualitzat", Toast.LENGTH_SHORT).show()
                        },
                        onError = { err -> Toast.makeText(context, err, Toast.LENGTH_SHORT).show() }
                    )
                }
            },
            confirmButton = { }, // botó dins UserForm
            dismissButton = { TextButton(onClick = { viewModel.dismissUserEditDialog() }) { Text("Tancar") } }
        )
    }

    // Diàleg per veure/editar el current user
    currentUserDialog?.let { u ->
        AlertDialog(
            onDismissRequest = { viewModel.dismissCurrentUserDialog() },
            title = { Text("Les meves dades") },
            text = {
                UserForm(user = u) { id, username, name, lastName, email, phone, roleId, password ->
                    viewModel.updateUser(
                        userId = id,
                        username = username,
                        name = name,
                        lastName = lastName,
                        email = email,
                        phone = phone,
                        roleId = roleId,
                        password = password,
                        onSuccess = {
                            viewModel.dismissCurrentUserDialog()
                            Toast.makeText(context, "Dades actualitzades", Toast.LENGTH_SHORT).show()
                        },
                        onError = { err -> Toast.makeText(context, err, Toast.LENGTH_SHORT).show() }
                    )
                }
            },
            confirmButton = { },
            dismissButton = { TextButton(onClick = { viewModel.dismissCurrentUserDialog() }) { Text("Tancar") } }
        )
    }
}

@Composable
fun RoleDropdown(selectedRole: Int, onSelect: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val roles = listOf("Tots" to 0, "Admin" to 1, "Professor" to 2, "Alumne" to 3)
    Box {
        Button(onClick = { expanded = true }) {
            Text("Filtrar: ${roles.firstOrNull { it.second == selectedRole }?.first ?: "Tots"}")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            roles.forEach { (name, id) ->
                DropdownMenuItem(text = { Text(name) }, onClick = { onSelect(id); expanded = false })
            }
        }
    }
}

@Composable
fun UserRow(user: User, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text("Usuari: ${user.username}", style = MaterialTheme.typography.titleMedium)
            Text("Nom: ${user.name} ${user.last_name}")
            Text("Email: ${user.email ?: "—"}")
            Text("Telèfon: ${user.phone ?: "—"}")
            Text("Rol ID: ${user.role_id}")
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Button(onClick = onEdit) { Text("Editar") }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onDelete) { Text("Eliminar") }
            }
        }
    }
}