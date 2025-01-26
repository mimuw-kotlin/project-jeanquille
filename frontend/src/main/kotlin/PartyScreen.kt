import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun PartyScreen(party: Party, userId: String, onNavigate: (Screen) -> Unit) {
    var localParty by remember { mutableStateOf(party) }
    var showAddMemberDialog by remember { mutableStateOf(false) }
    var friends by remember { mutableStateOf<List<Account>?>(null) }

    fun reloadParty() {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                localParty = fetchParty(party.id)
                friends = fetchFriends(userId)
                println (localParty.name)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    LaunchedEffect(Unit) {
        friends = fetchFriends(userId)
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
            AddMemberDialog({ showAddMemberDialog = false }, localParty, friends!!)
        }
    }
}

@Composable
fun AddMemberDialog(onDismiss: () -> Unit, party: Party, friends: List<Account>) {
    var expanded by remember { mutableStateOf(false) }
    var chosenOption: Account? by remember { mutableStateOf(null) }
    var showError by remember { mutableStateOf(false) }
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .wrapContentSize(Alignment.Center)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(color = MaterialTheme.colors.background, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Text(text = "Choose a friend to add")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { expanded = !expanded },
                    ) {
                    Text(chosenOption?.username ?: "Click here to choose")
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    friends.forEach { friend ->
                        DropdownMenuItem(
                            onClick = {
                                chosenOption = friend
                                expanded = !expanded
                            }
                        ) {
                            Text(friend.username)
                        }
                    }
                }
                Button(
                    onClick = {
                        if (chosenOption == null) {
                            showError = true
                        }
                        else {
                            CoroutineScope(Dispatchers.Default).launch {
                                addMember(party.id, chosenOption!!.id)
                            }
                            onDismiss()
                        }
                    }
                ) {
                    Text("Confirm")
                }
                Button(onClick = onDismiss) {
                    Text("Cancel")
                }
                if (showError) {
                    Text("Error: choose a friend before clicking \"Confirm\"")
                }
            }
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