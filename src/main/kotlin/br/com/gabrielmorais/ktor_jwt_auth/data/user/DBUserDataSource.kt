package br.com.gabrielmorais.ktor_jwt_auth.data.user

class DBUserDataSource : UserDataSource {
  private val listUsers = mutableListOf<User>()
  override suspend fun getUserByUsername(username: String): User? {
    return listUsers.find { it.username == username }
  }

  override suspend fun insertUser(user: User): Boolean {
    return listUsers.add(user)
  }
}