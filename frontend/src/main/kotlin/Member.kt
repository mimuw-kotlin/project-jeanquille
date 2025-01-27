import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.Serializable

@Serializable
data class Member(
    var id: Long,
    var account: Account,
    var balance: Long
)

suspend fun removeMember(memberId: Long): HttpResponse {
    val response: HttpResponse = client.delete("http://localhost:8080/member/$memberId")
    return response
}
