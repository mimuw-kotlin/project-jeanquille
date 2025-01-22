import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PartyScreen() {
    var showAddMemberDialog by remember { mutableStateOf(false) }

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
                    text = "Party1",
                    fontSize = 24.sp
                )
                IconButton(
                    onClick = {},
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
                    MembersList({ showAddMemberDialog = true })
                }
                Box(modifier = Modifier.weight(1f)) {
                    BillsList({})
                }
                Box(modifier = Modifier.weight(1f)) {
                    TransactionsList()
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
fun MembersList(onButtonClick: () -> Unit) {
    val listOfMembers = listOf<@Composable () -> Unit>(
        { Text("Meten")},
        { Text("Matdas")},
        { Text("Avamaco")}
    )
    PrettyList("Members", true, "Add member", onButtonClick, listOfMembers)
}

@Composable
fun BillsList(onButtonClick: () -> Unit) {
    val listOfBills = listOf<@Composable () -> Unit>(
        { Text("Bill1")},
        { Text("Bill2")},
        { Text("Bill3")}
    )
    PrettyList("Bills", true, "Add bill", onButtonClick, listOfBills)
}

@Composable
fun TransactionsList() {
    val listOfTransactions = listOf<@Composable () -> Unit>(
        { Text("Transaction1")},
        { Text("Transaction2")},
        { Text("Transaction3")}
    )
    PrettyList("Transactions", false, content = listOfTransactions)
}