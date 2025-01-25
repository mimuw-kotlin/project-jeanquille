import io.ktor.client.call.*
import io.ktor.client.request.*
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

suspend fun fetchParties(): List<Party> {
    val url = "http://localhost:8080/parties"
    return client.get(url) {
        contentType(ContentType.Application.Json)
    }.body() // ListSerializer(Party.serializer()) is applied automatically
}