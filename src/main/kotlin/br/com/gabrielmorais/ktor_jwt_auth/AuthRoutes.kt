package br.com.gabrielmorais.ktor_jwt_auth

import br.com.gabrielmorais.ktor_jwt_auth.data.request.AuthRequest
import br.com.gabrielmorais.ktor_jwt_auth.data.responses.AuthResponse
import br.com.gabrielmorais.ktor_jwt_auth.security.hashing.HashingService
import br.com.gabrielmorais.ktor_jwt_auth.data.user.User
import br.com.gabrielmorais.ktor_jwt_auth.data.user.UserDataSource
import br.com.gabrielmorais.ktor_jwt_auth.security.hashing.SaltedHash
import br.com.gabrielmorais.ktor_jwt_auth.security.token.TokenClaim
import br.com.gabrielmorais.ktor_jwt_auth.security.token.TokenConfig
import br.com.gabrielmorais.ktor_jwt_auth.security.token.TokenService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.apache.commons.codec.digest.DigestUtils

fun Route.signup(
  hashingService: HashingService,
  userDataSource: UserDataSource,
  tokenService: TokenService,
  tokenConfig: TokenConfig
) {
  post("signup") {
    val request = call.receive<AuthRequest>()

    val areFieldsBlank = request.username.isBlank() || request.password.isBlank()
    val isPwTooShort = request.password.length < 8
    if (areFieldsBlank || isPwTooShort) {
      call.respond(HttpStatusCode.Conflict)
      return@post
    }

    val saltedHash = hashingService.generateSaltedHash(request.password)
    val user = User(
      username = request.username,
      password = saltedHash.hash,
      salt = saltedHash.salt
    )

    val wasAcknowledged = userDataSource.insertUser(user)
    if (!wasAcknowledged) {
      call.respond(HttpStatusCode.Conflict)
      return@post
    }

    call.respond(HttpStatusCode.OK)
  }
}

fun Route.signin(
  hashingService: HashingService,
  userDataSource: UserDataSource,
  tokenService: TokenService,
  tokenConfig: TokenConfig
) {
  post("signin") {
    val request = call.receive<AuthRequest>()
    val user = userDataSource.getUserByUsername(request.username)
    if (user == null) {
      call.respond(HttpStatusCode.Conflict, "Usuário ou senha incorretos")
      return@post
    }

    val isValidPassword = hashingService.verify(
      value = request.password,
      saltedHash = SaltedHash(
        hash = user.password,
        salt = user.salt
      )
    )

    if (!isValidPassword) {
      println("Hash: ${DigestUtils.sha256Hex("${user.salt}${request.password}")}, Hashed PW: ${user.password}")
      call.respond(HttpStatusCode.Conflict, "Usuário ou senha incorretos")
      return@post
    }

    val token = tokenService.generate(
      config = tokenConfig,
      TokenClaim(
        name = "userId",
        value = user.id.toString()
      )
    )

    call.respond(
      status = HttpStatusCode.OK,
      message = AuthResponse(
        token = token
      )
    )
  }
}

fun Route.authenticate() {
  authenticate {
    get("authenticate") {
      call.respond(HttpStatusCode.OK)
    }
  }
}

fun Route.getSecretInfo() {
  authenticate {
    get("secret") {
      val principal = call.principal<JWTPrincipal>()
      val userId = principal?.getClaim("userId", String::class)
      call.respond(HttpStatusCode.OK, "Seu usuário é $userId")
    }
  }
}