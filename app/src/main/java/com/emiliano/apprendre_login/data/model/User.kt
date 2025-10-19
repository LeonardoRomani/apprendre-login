package com.emiliano.apprendre_login.data.model

/**
 * Classe de dades que representa un usuari del sistema.
 *
 * Aquesta classe modela la informació bàsica d'un usuari amb tots els camps
 * necessaris per al funcionament de l'aplicació d'aprenentatge.
 *
 * @property user_id Identificador únic de l'usuari a la base de dades
 * @property username Nom d'usuari per a l'accés al sistema
 * @property name Nom real de l'usuari
 * @property last_name Cognom de l'usuari
 * @property email Adreça de correu electrònic (opcional)
 * @property phone Número de telèfon (opcional)
 * @property role_id Identificador del rol de l'usuari (1=Admin, 2=Professor, 3=Student, 4=Parent)
 * @property last_login Data de l'últim accés de l'usuari (opcional)
 */
data class User(
    val user_id: Int?,
    val username: String,
    val name: String,
    val last_name: String,
    val email: String?,
    val phone: String?,
    val role_id: Int?,
    val last_login: String?
)