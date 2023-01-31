package br.com.gabrielmorais.ktor_jwt_auth.security.token

interface TokenService {
  fun generate(config: TokenConfig, vararg claims: TokenClaim): String
}