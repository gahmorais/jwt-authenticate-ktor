package br.com.gabrielmorais.ktor_jwt_auth.data.user

import kotlin.random.Random

data class User(
  val id: Int = Random.nextInt(),
  val username: String,
  val password: String,
  val salt: String
)
