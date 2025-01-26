import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Bill(
    val id: Long,
    val name: String,
    val amount: Long,
    val date: String,
    val payer: Account,
    val participants: List<Account>
)