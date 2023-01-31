package br.com.gabrielmorais.ktor_jwt_auth

import br.com.gabrielmorais.ktor_jwt_auth.data.user.DBUserDataSource
import br.com.gabrielmorais.ktor_jwt_auth.security.token.JwtTokenService
import br.com.gabrielmorais.ktor_jwt_auth.security.token.TokenConfig
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main(args: Array<String>) = EngineMain.main(args)

@Suppress("unused")
fun Application.server() {
  val dbUser = DBUserDataSource()
  val tokenService = JwtTokenService()

}