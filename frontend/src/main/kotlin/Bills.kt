import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Bill(
    val name: String,
    val amount: String,
    val participantsIds: List<String>
)