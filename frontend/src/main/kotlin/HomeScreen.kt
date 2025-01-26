import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
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
fun HomeScreen(userId: String, onNavigate: (Screen) -> Unit, onSetParty: (Party) -> Unit) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showAddPartyDialog by remember { mutableStateOf(false) }
    var showAddFriendDialog by remember { mutableStateOf(false) }
    var removedFriend: Account? by remember { mutableStateOf(null) }
    var user: Account? by remember { mutableStateOf(null) }

    val error = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            user = fetchAccount(userId)
        } catch (e: Exception) {
            error.value = e.message
        }
    }

    if (error.value != null) {
        Text("Error: ${error.value}", color = MaterialTheme.colors.error)
        return
    } else if (user == null) {
        CircularProgressIndicator()
        return
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
                    text = user!!.username,
                    fontSize = 24.sp
                )
                IconButton(
                    onClick = {showLogoutDialog = true},
                    modifier = Modifier.background(MaterialTheme.colors.secondary, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ExitToApp,
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
                    PartiesList({ showAddPartyDialog = true }, userId, onSetParty, onNavigate)
                }
                Box(modifier = Modifier.weight(1f)) {
                    FriendsList({ showAddFriendDialog = true }, userId, { friend -> removedFriend = friend })
                }
            }
        }
        if (showLogoutDialog) {
            LogoutDialog(onDismiss = { showLogoutDialog = false }, onLogout = { onNavigate(Screen.Login) })
        }
        if (showAddPartyDialog) {
            TextFieldDialog(
                question = "Enter party name",
                onAccept = {partyName: String ->
                    CoroutineScope(Dispatchers.IO).launch {
                        createParty(userId, partyName)
                    }
                    showAddPartyDialog = false
                },
                onDismiss = { showAddPartyDialog = false }
            )
        }
        if (showAddFriendDialog) {
            TextFieldDialog(
                question = "Enter friend name",
                onAccept = {friendName: String ->
                    CoroutineScope(Dispatchers.IO).launch {
                        addFriend(userId, friendName)
                    }
                    showAddFriendDialog = false
                },
                onDismiss = { showAddFriendDialog = false }
            )
        }
        if (removedFriend != null) {
            RemoveFriendDialog(
                { removedFriend = null },
                user!!.id,
                removedFriend!!
            )
        }
    }
}

@Composable
fun YesNoDialog(question: String, yesAnswer: String, noAnswer: String, onAccept: () -> Unit, onDismiss: () -> Unit) {
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
                Text(text = question)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onAccept) {
                    Text(yesAnswer)
                }
                Button(onClick = onDismiss) {
                    Text(noAnswer)
                }
            }
        }
    }
}

@Composable
fun LogoutDialog(onDismiss: () -> Unit, onLogout: () -> Unit) {
    YesNoDialog(
        "Do you really want to log out?",
        "Log out",
        "Cancel",
        onLogout,
        onDismiss
    )
}

@Composable
fun RemoveFriendDialog(onDismiss: () -> Unit, userId: String, friend: Account) {
    YesNoDialog(
        "Do you really want to unfriend ${friend.username}?",
        "Yes",
        "No",
        {
            GlobalScope.launch {
                removeFriend(userId, friend.id)
            }
            onDismiss()
        },
        onDismiss
    )
}

@Composable
fun TextFieldDialog(question: String, onAccept: (String) -> Unit, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        var text by remember { mutableStateOf("") }
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
                Text(text = question)
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Name") }
                )
                Button(onClick = { onAccept(text) }) {
                    Text("Accept")
                }
                Button(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        }
    }
}

@Composable
fun ListHeader(name: String, showButton: Boolean, buttonDescription: String = "", onButtonClick: () -> Unit = {}) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(16.dp).fillMaxWidth()
    ) {
        Text(
            text = name,
            fontSize = 24.sp
        )
        if (showButton) {
            IconButton(
                onClick = onButtonClick,
                modifier = Modifier.background(MaterialTheme.colors.secondary, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = buttonDescription,
                    tint = MaterialTheme.colors.primary
                )
            }
        }
    }
}

@Composable
fun PrettyListContent(content: List<@Composable () -> Unit>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        for (element in content) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colors.secondaryVariant, RoundedCornerShape(8.dp))
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                element()
            }
        }
    }
}

@Composable
fun PartyCard(party: Party, onSetParty: (Party) -> Unit, onNavigate: (Screen) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = party.name
        )
        IconButton(
            onClick = {
                onSetParty(party)
                onNavigate(Screen.Party)
            }
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Info",
                tint = MaterialTheme.colors.primary
            )
        }
    }
}

@Composable
fun FriendCard(friend: Account, onRemoveFriend: (Account) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = friend.username
        )
        if (friend.phoneNumber != "") {
            Row {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "Phone number",
                    tint = MaterialTheme.colors.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(friend.phoneNumber)
            }
        }

        IconButton(
            onClick = { onRemoveFriend(friend) }
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Unfriend",
                tint = MaterialTheme.colors.primary
            )
        }
    }
}

@Composable
fun PrettyList(name: String,
               showButton: Boolean,
               buttonDescription: String = "",
               onButtonClick: () -> Unit = {},
               content: List<@Composable () -> Unit>) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.background, RoundedCornerShape(16.dp))
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        ListHeader(name, showButton, buttonDescription, onButtonClick)
        PrettyListContent(content)
    }
}

@Composable
fun PartiesList(onButtonClick: () -> Unit, userId: String, onSetParty: (Party) -> Unit, onNavigate: (Screen) -> Unit) {
    var parties by remember { mutableStateOf<List<Party>?>(null) }
    LaunchedEffect(Unit) {
        parties = fetchParties(userId)
    }
    if (parties == null) {
        return
    }

    val listOfParties = parties!!.map { party: Party -> @Composable{ PartyCard(party, onSetParty, onNavigate) } }
    PrettyList("Parties", true, "Create party", onButtonClick, listOfParties)
}

@Composable
fun FriendsList(onButtonClick: () -> Unit, userId: String, onRemoveFriend: (Account) -> Unit) {
    var friends by remember { mutableStateOf<List<Account>?>(null) }
    LaunchedEffect(Unit) {
        friends = fetchFriends(userId)
    }
    if (friends == null) {
        return
    }

    val listOfFriends = friends!!.map { friend: Account -> @Composable{ FriendCard(friend, onRemoveFriend) } }
    PrettyList("Friends", true, "Add friend", onButtonClick, listOfFriends)
}