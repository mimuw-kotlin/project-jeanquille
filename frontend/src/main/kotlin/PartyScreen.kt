import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun PartyScreen(party: Party, userId: String, onNavigate: (Screen) -> Unit) {
    var localParty by remember { mutableStateOf(party) }
    var showAddMemberDialog by remember { mutableStateOf(false) }
    var showTransactionInfoDialog : Transaction? by remember { mutableStateOf(null) }
    var showBillInfoDialog : Bill? by remember { mutableStateOf(null) }
    var showBillRemoveDialog : Bill? by remember { mutableStateOf(null) }
    var showMemberRemoveDialog: Member? by remember { mutableStateOf(null) }
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
                    MembersList({ showAddMemberDialog = true }, localParty) { showMemberRemoveDialog = it }
                }
                Box(modifier = Modifier.weight(1f)) {
                    BillsList({}, localParty, { showBillInfoDialog = it }, { showBillRemoveDialog = it })
                }
                Box(modifier = Modifier.weight(1f)) {
                    TransactionsList(localParty) { showTransactionInfoDialog = it }
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
        if (showTransactionInfoDialog != null) {
            InfoDialog(
                text = "Payer: " + showTransactionInfoDialog!!.payer.username + "\n" +
                        "Receiver: " + showTransactionInfoDialog!!.receiver.username + "\n" +
                        "Amount: " + showTransactionInfoDialog!!.amount + "\n" +
                        "Receiver phone number: " + showTransactionInfoDialog!!.receiver.phoneNumber,

                onDismiss = { showTransactionInfoDialog = null }
            )
        }

        if (showBillInfoDialog != null) {
            InfoDialog(
                text = "Name: " + showBillInfoDialog!!.name + "\n" +
                        "Date: " + showBillInfoDialog!!.date + "\n" +
                        "Amount: " + showBillInfoDialog!!.amount + "\n" +
                        "Payer: " + showBillInfoDialog!!.payer.username + "\n" +
                        "Participants: " + showBillInfoDialog!!.participants.map { it.username }.joinToString(", "),

                onDismiss = { showBillInfoDialog = null }
            )
        }

        if (showBillRemoveDialog != null) {
            YesNoDialog(
                question = "Are you sure you want to remove this bill?",
                onAccept = {
                    GlobalScope.launch {
                        removeBill(showBillRemoveDialog!!.id)
                        reloadParty()
                        showBillRemoveDialog = null
                    }
                },
                onDismiss = { showBillRemoveDialog = null },
                yesAnswer = "Yes",
                noAnswer = "No",
            )
        }

        if (showMemberRemoveDialog != null) {
            YesNoDialog(
                question = "Are you sure you want to remove this member?",
                onAccept = {
                    GlobalScope.launch {
                        removeMember(showMemberRemoveDialog!!.id)
                        reloadParty()
                        showMemberRemoveDialog = null
                    }
                },
                onDismiss = { showMemberRemoveDialog = null },
                yesAnswer = "Yes",
                noAnswer = "No",
            )
        }
    }
}

@Composable
fun MembersList(onButtonClick: () -> Unit, party: Party, onRemoveMember: (Member) -> Unit) {
    val listOfMembers = party.members.map { member: Member -> @Composable{ MemberCard(member, onRemoveMember) } }

    PrettyList("Members", true, "Add member", onButtonClick, listOfMembers)
}

@Composable
fun BillsList(onButtonClick: () -> Unit, party: Party, onInfoClick: (Bill) -> Unit, onRemoveBill: (Bill) -> Unit) {
    val listOfBills = party.bills.map { bill: Bill -> @Composable{ BillCard(bill, onInfoClick, onRemoveBill ) } }
    PrettyList("Bills", true, "Add bill", onButtonClick, listOfBills)
}

@Composable
fun TransactionsList(party: Party, onButtonClick: (Transaction) -> Unit) {
    val listOfTransactions = party.transactions.map { transaction: Transaction -> @Composable{ TransactionCard(transaction, onButtonClick) } }
    PrettyList("Transactions", false, content = listOfTransactions)
}

@Composable
fun TransactionCard(transaction: Transaction, onButtonClick: (Transaction) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = transaction.payer.username + " owes " + transaction.receiver.username + " " + transaction.amount.toString(),
        )

        IconButton(
            onClick = { onButtonClick(transaction) }
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Transaction Info",
                tint = MaterialTheme.colors.primary
            )
        }
    }
}

@Composable
fun BillCard(bill: Bill, onInfoClick: (Bill) -> Unit, onRemoveBill: (Bill) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = bill.name + " " + bill.amount.toString() + "zł"
        )

        IconButton(
            onClick = { onInfoClick(bill) }
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Bill info",
                tint = MaterialTheme.colors.primary
            )
        }

        IconButton(
            onClick = { onRemoveBill(bill) }
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete bill",
                tint = MaterialTheme.colors.primary
            )
        }
    }
}

@Composable
fun MemberCard(member: Member, onRemoveMember: (Member) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = member.account.username
        )

        Text(
            text = member.balance.toString() + "zł"
        )

        IconButton(
            onClick = { onRemoveMember(member) }
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Remove member",
                tint = MaterialTheme.colors.primary
            )
        }
    }
}