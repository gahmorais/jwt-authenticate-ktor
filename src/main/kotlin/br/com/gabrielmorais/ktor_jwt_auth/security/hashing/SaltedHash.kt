package br.com.gabrielmorais.ktor_jwt_auth.security.hashing

data class SaltedHash(
  val hash: String,
  val salt: String
)
