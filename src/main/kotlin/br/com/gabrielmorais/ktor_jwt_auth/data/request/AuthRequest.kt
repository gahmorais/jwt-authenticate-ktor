package br.com.gabrielmorais.ktor_jwt_auth.data.request

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
  val username: String,
  val password: String
)