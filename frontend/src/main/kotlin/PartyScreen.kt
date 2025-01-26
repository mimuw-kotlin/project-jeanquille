import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun PartyScreen(party: Party, onNavigate: (Screen) -> Unit) {
    var localParty by remember { mutableStateOf(party) }
    var showAddMemberDialog by remember { mutableStateOf(false) }

    fun reloadParty() {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                localParty = fetchParty(party.id)
                println (localParty.name)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(MaterialTheme.colors.background)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = localParty.name,
                    fontSize = 24.sp
                )
                IconButton(
                    onClick = { reloadParty() },
                    modifier = Modifier.background(MaterialTheme.colors.secondary, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh party",
                        tint = MaterialTheme.colors.primary
                    )
                }
                IconButton(
                    onClick = { onNavigate(Screen.Home) },
                    modifier = Modifier.background(MaterialTheme.colors.secondary, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Logout",
                        tint = MaterialTheme.colors.primary
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    MembersList({ showAddMemberDialog = true }, localParty)
                }
                Box(modifier = Modifier.weight(1f)) {
                    BillsList({}, localParty)
                }
                Box(modifier = Modifier.weight(1f)) {
                    TransactionsList(localParty)
                }
            }
        }
        if (showAddMemberDialog) {
            TextFieldDialog(
                question = "Enter member name",
                onAccept = { showAddMemberDialog = false },
                onDismiss = { showAddMemberDialog = false }
            )
        }
    }
}

@Composable
fun MembersList(onButtonClick: () -> Unit, party: Party) {
    val listOfMembers = party.members.map { member: Member -> @Composable{ Text(member.account.username) } }

    PrettyList("Members", true, "Add member", onButtonClick, listOfMembers)
}

@Composable
fun BillsList(onButtonClick: () -> Unit, party: Party) {
    val listOfBills = party.bills.map { bill: Bill -> @Composable{ Text(bill.name) } }
    PrettyList("Bills", true, "Add bill", onButtonClick, listOfBills)
}

@Composable
fun TransactionsList(party: Party) {
    val listOfTransactions = party.transactions.map { transaction: Transaction -> @Composable{ Text(transaction.id.toString()) } }
    PrettyList("Transactions", false, content = listOfTransactions)
}