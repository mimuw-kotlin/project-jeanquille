import kotlinx.serialization.Serializable

@Serializable
data class Member (
    var id: Long? = null,
    var account: Account,
    var balance: Long
)