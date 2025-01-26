import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

@Serializable
data class Party(
    val id: Long,
    val name: String,
    val creationDate: String,
    val members: List<Member>,
    val bills: List<Bill>,
    val transactions: List<Transaction>
)

suspend fun fetchParties(userId: String): List<Party> {
    val url = "http://localhost:8080/parties/$userId"
    return client.get(url) {
        contentType(ContentType.Application.Json)
    }.body() // ListSerializer(Party.serializer()) is applied automatically
}

suspend fun fetchParty(partyId: Long): Party {
    val url = "http://localhost:8080/party/$partyId"
    return client.get(url) {
        contentType(ContentType.Application.Json)
    }.body()
}

suspend fun createParty(userId: String, partyName: String): String {
    var responseMessage: String
    try {
        val response: HttpResponse = client.post("http://localhost:8080/party/$userId") {
            contentType(ContentType.Application.Json)
            setBody(
                mapOf(
                    "name" to partyName
                )
            )
        }
        responseMessage = if (response.status == HttpStatusCode.OK) {
            "Create party successful!"
        } else {
            "Create party failed: ${response.status}"
        }
    } catch (e: Exception) {
        responseMessage = "Error: ${e.message}"
    }
    return responseMessage
}

suspend fun addMember(partyId: Long, friendId: String): HttpResponse {
    val response: HttpResponse = client.post("http://localhost:8080/party/$partyId/member/$friendId")
    return response
}