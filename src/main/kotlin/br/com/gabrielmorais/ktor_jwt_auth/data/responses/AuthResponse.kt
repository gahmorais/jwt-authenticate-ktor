package br.com.gabrielmorais.ktor_jwt_auth.data.responses

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
  val token: String
)