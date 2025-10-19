
//https://apprendre-servidor.onrender.com/docs

package com.emiliano.apprendre_login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.emiliano.apprendre_login.ui.admin.AdminHomeScreen
import com.emiliano.apprendre_login.ui.login.LoginScreen
import com.emiliano.apprendre_login.ui.login.RegisterScreen
import com.emiliano.apprendre_login.ui.theme.ApprendreLoginTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ApprendreLoginTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        // ----------------------------
        // Pantalla de Login
        // ----------------------------
        composable("login") {
            LoginScreen(
                onNavigateToRegister = { navController.navigate("register") },

                // Rol 1 - Administrador
                onNavigateToAdmin = {
                    navController.navigate("admin_home") {
                        popUpTo("login") { inclusive = true }
                    }
                },

                // Rol 2 - Professor
                onNavigateToTeacher = {
                    navController.navigate("teacher_home") {
                        popUpTo("login") { inclusive = true }
                    }
                },

                // Rol 3 - Alumne
                onNavigateToStudent = {
                    navController.navigate("student_home") {
                        popUpTo("login") { inclusive = true }
                    }
                },

                // 🔹 Rol 4 - Parent (nou)
                onNavigateToParent = {
                    navController.navigate("parent_home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // ----------------------------
        // Pantalla de Registre
        // ----------------------------
        composable("register") {
            RegisterScreen(navController = navController)
        }

        // ----------------------------
        // Pantalles segons el rol
        // ----------------------------

        composable("admin_home") {
            AdminHomeScreen(
                onLogout = {
                    navController.navigate("login") {
                        popUpTo(0)
                    }
                }
            )
        }

        composable("teacher_home") {
            TeacherHomeScreen(
                onLogout = {
                    navController.navigate("login") {
                        popUpTo(0)
                    }
                }
            )
        }

        composable("student_home") {
            StudentHomeScreen(
                onLogout = {
                    navController.navigate("login") {
                        popUpTo(0)
                    }
                }
            )
        }

        // 🔹 Nova pantalla per al rol 4
        composable("parent_home") {
            ParentHomeScreen(
                onLogout = {
                    navController.navigate("login") {
                        popUpTo(0)
                    }
                }
            )
        }
    }
}

@Composable
fun TeacherHomeScreen(onLogout: () -> Unit) {
    RoleHomeScreen(
        title = "Pantalla del Professor",
        onLogout = onLogout
    )
}

@Composable
fun StudentHomeScreen(onLogout: () -> Unit) {
    RoleHomeScreen(
        title = "Pantalla de l'Alumne",
        onLogout = onLogout
    )
}

// 🔹 Nova pantalla per al rol 4 (Parent)
@Composable
fun ParentHomeScreen(onLogout: () -> Unit) {
    RoleHomeScreen(
        title = "Pantalla del Pare/Mare",
        onLogout = onLogout
    )
}

// 🔹 Component reutilitzable per a pantalles de rols
@Composable
fun RoleHomeScreen(title: String, onLogout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(title, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onLogout) {
            Text("Tancar Sessió")
        }
    }
}
