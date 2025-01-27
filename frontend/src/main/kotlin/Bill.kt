import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.Serializable

@Serializable
data class Bill(
    val id: Long,
    val name: String,
    val amount: Long,
    val date: String,
    val payer: Account,
    val participants: List<Account>
)

suspend fun removeBill(billId: Long): HttpResponse {
    val response: HttpResponse = client.delete("http://localhost:8080/bill/$billId")
    return response
}
