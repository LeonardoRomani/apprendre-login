package com.emiliano.apprendre_login.ui.admin

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.emiliano.apprendre_login.data.model.User

@Composable
fun UserItem(
    user: User,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text("Usuari: ${user.username}", style = MaterialTheme.typography.titleMedium)
            Text("Nom: ${user.name} ${user.last_name}")
            Text("Email: ${user.email}")
            Text("Telèfon: ${user.phone}")
            Text("Rol ID: ${user.role_id}")
            Text("Última connexió: ${user.last_login ?: "Desconeguda"}")

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                Button(onClick = onEdit) {
                    Text("Editar")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(onClick = onDelete) {
                    Text("Eliminar")
                }
            }
        }
    }
}

@Composable
fun UserList(
    users: List<User>,
    onEditUser: (User) -> Unit,
    onDeleteUser: (User) -> Unit
) {
    LazyColumn {
        items(users) { user ->
            UserItem(
                user = user,
                onEdit = { onEditUser(user) },
                onDelete = { onDeleteUser(user) }
            )
        }
    }
}