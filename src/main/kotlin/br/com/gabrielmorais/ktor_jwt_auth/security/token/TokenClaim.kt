package br.com.gabrielmorais.ktor_jwt_auth.security.token

data class TokenClaim(
  val name: String,
  val value: String
)
