import kotlinx.serialization.Serializable

@Serializable
data class Transaction(
    val id: Long,
    val amount: Long,
    val party: Party,
    val payer: Account,
    val receiver: Account
)