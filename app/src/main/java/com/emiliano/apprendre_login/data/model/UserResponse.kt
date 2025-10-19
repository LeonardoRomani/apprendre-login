package com.emiliano.apprendre_login.data.model

/**
 * Classe de dades que representa la resposta de l'API quan es sol·licita una llista d'usuaris.
 *
 * Aquesta classe s'utilitza per parsejar les respones JSON de l'API que poden tenir
 * diferents estructures. Algunes APIs retornen la llista d'usuaris sota la propietat "users",
 * altres sota "data", i algunes inclouen un missatge addicional.
 *
 * @property users Llista d'usuaris retornada sota la propietat "users" (opcional)
 * @property data Llista d'usuaris retornada sota la propietat "data" (opcional)
 * @property message Missatge addicional de la resposta (opcional)
 */
data class UserResponse(
    val users: List<User>?,
    val data: List<User>?,
    val message: String?
)