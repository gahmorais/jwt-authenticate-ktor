package br.com.gabrielmorais.ktor_jwt_auth.security.token

data class TokenConfig(
  val issuer: String,
  val audience: String,
  val expiresIn: String,
  val secret: String
)