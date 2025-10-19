package com.emiliano.apprendre_login.data.model

/**
 * Classe de dades que representa la resposta de l'API quan un usuari intenta fer login.
 *
 * Aquesta classe encapsula la resposta del servidor després d'un intent d'autenticació.
 * Conté el token d'accés JWT i la informació de l'usuari si l'autenticació és exitosa,
 * o un missatge d'error en cas de fallar.
 *
 * @property access_token Token JWT que s'utilitza per autoritzar peticions posteriors (opcional)
 * @property token_type Tipus de token (normalment "bearer") (opcional)
 * @property user_id ID de l'usuari retornat pel servidor (opcional)
 * @property username Nom d'usuari retornat pel servidor (opcional)
 * @property role Rol de l'usuari (1=Admin, 2=Professor, 3=Alumne) (opcional)
 * @property errorMessage Missatge d'error en cas que el login falli (opcional)
 */
data class LoginResponse(
    val access_token: String?,     // Token JWT que s'utilitza per autoritzar peticions posteriors
    val token_type: String?,       // Tipus de token (normalment "bearer")
    val user_id: String?,          // ID de l'usuari retornat pel servidor
    val username: String?,         // Nom d'usuari retornat pel servidor
    val role: Int?,                // Rol assignat a l'usuari (1-Admin, 2-Teacher, 3-Teacher, 4-Parent)
    val errorMessage: String? = null // Missatge d'error en cas que el login falli
)