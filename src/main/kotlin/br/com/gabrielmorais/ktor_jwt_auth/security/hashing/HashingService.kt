package br.com.gabrielmorais.ktor_jwt_auth.security.hashing

interface HashingService {
  fun generateSaltedHash(value: String, saltLength: Int = 32): SaltedHash
  fun verify(value: String, saltedHash: SaltedHash): Boolean
}