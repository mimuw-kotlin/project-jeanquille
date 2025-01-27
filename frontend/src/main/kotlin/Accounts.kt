import java.util.UUID
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.utils.EmptyContent.contentType
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable

@Serializable
data class Account(
    val id: String,
    val username: String,
    val phoneNumber: String
)

data class LoginResult(
    val success: Boolean,
    val uuid: String = "",
    val errorMessage: String = ""
)

suspend fun registerUser(username: String, password: String, phoneNumber: String): String {
    var responseMessage: String
    try {
        val response: HttpResponse = client.post("http://localhost:8080/register") {
            contentType(ContentType.Application.Json)
            setBody(
                mapOf(
                    "username" to username,
                    "password" to password,
                    "phoneNumber" to phoneNumber
                )
            )
        }
        responseMessage = if (response.status == HttpStatusCode.OK) {
            "Registration successful!"
        } else {
            "Registration failed: ${response.status}"
        }
    } catch (e: Exception) {
        responseMessage = "Error: ${e.message}"
    }
    return responseMessage
}

suspend fun loginUser(username: String, password: String): LoginResult {
    try {
        val response: HttpResponse = client.post("http://localhost:8080/login") {
            contentType(ContentType.Application.Json)
            setBody(
                mapOf(
                    "username" to username,
                    "password" to password
                )
            )
        }
        if (response.status == HttpStatusCode.OK) {
            var userId: String = response.body()
            userId = userId.substring(1, userId.length - 1) // cut quote marks
            return LoginResult(true, uuid = userId)
        } else if (response.status == HttpStatusCode.Unauthorized) {
            return LoginResult(false, errorMessage = "Invalid credentials")
        } else {
            return LoginResult(false, errorMessage = "Login failed: ${response.status}")
        }
    } catch (e: Exception) {
        return  LoginResult(false, errorMessage = e.message ?: "empty error message")
    }
}

suspend fun fetchAccount(userId: String): Account {
    val url = "http://localhost:8080/account/$userId"
    return client.get(url) {
        contentType(ContentType.Application.Json)
    }.body()
}

suspend fun addFriend(userId: String, friendName: String): HttpResponse {
    val response: HttpResponse = client.post("http://localhost:8080/friends/$userId/add") {
        contentType(ContentType.Application.Json)
        setBody(
            mapOf(
                "friendUsername" to friendName
            )
        )
    }
    if (response.status == HttpStatusCode.OK) {
        return response
    } else {
        throw Exception("Failed to add friend: ${response.body<Map<String, String>>()["message"]}")
    }
}

suspend fun removeFriend(userId: String, friendId: String): HttpResponse {
    val response: HttpResponse = client.delete("http://localhost:8080/friends/$userId/unfriend/$friendId")
    return response
}

suspend fun fetchFriends(userId: String): List<Account> {
    val url = "http://localhost:8080/friends/$userId"
    return client.get(url) {
        contentType(ContentType.Application.Json)
    }.body() // ListSerializer(Party.serializer()) is applied automatically
}