import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    var deletedParty: Party? by remember { mutableStateOf(null) }
    var user: Account? by remember { mutableStateOf(null) }
    var friends by remember { mutableStateOf<List<Account>?>(null) }
    var parties by remember { mutableStateOf<List<Party>?>(null) }

    val error = remember { mutableStateOf<String?>(null) }

    fun reloadHome() {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                parties = fetchParties(userId)
                friends = fetchFriends(userId)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    LaunchedEffect(Unit) {
        friends = fetchFriends(userId)
        parties = fetchParties(userId)
    }

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
                    onClick = {
                        CoroutineScope(Dispatchers.Default).launch {
                            friends = fetchFriends(userId)
                            parties = fetchParties(userId)
                        }
                    },
                    modifier = Modifier.background(MaterialTheme.colors.secondary, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        tint = MaterialTheme.colors.primary
                    )
                }
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
                    PartiesList(parties, { showAddPartyDialog = true }, onSetParty, onNavigate, { party -> deletedParty = party })
                }
                Box(modifier = Modifier.weight(1f)) {
                    FriendsList(friends, { showAddFriendDialog = true }, { friend -> removedFriend = friend })
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
                        reloadHome()
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
                        reloadHome()
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
                removedFriend!!,
                { reloadHome() }
            )
        }
        if (deletedParty != null) {
            DeletePartyDialog(
                { deletedParty = null },
                deletedParty!!,
                { reloadHome() }
            )
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
fun RemoveFriendDialog(onDismiss: () -> Unit, userId: String, friend: Account, reloadHome: () -> Unit) {
    YesNoDialog(
        "Do you really want to unfriend ${friend.username}?",
        "Yes",
        "No",
        {
            CoroutineScope(Dispatchers.IO).launch {
                removeFriend(userId, friend.id)
                reloadHome()
            }
            onDismiss()
        },
        onDismiss
    )
}

@Composable
fun DeletePartyDialog(onDismiss: () -> Unit, party: Party, reloadHome: () -> Unit) {
    YesNoDialog(
        "Do you really want to delete party ${party.name}?",
        "Yes",
        "No",
        {
            CoroutineScope(Dispatchers.IO).launch {
                deleteParty(party.id)
                reloadHome()
            }
            onDismiss()
        },
        onDismiss
    )
}

@Composable
fun PartyCard(party: Party, onSetParty: (Party) -> Unit, onNavigate: (Screen) -> Unit, onDeleteParty: (Party) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = party.name
        )
        Row {
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
            IconButton(
                onClick = {
                    onDeleteParty(party)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete party",
                    tint = MaterialTheme.colors.primary
                )
            }
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
fun PartiesList(parties: List<Party>?, onButtonClick: () -> Unit, onSetParty: (Party) -> Unit, onNavigate: (Screen) -> Unit, onDeleteParty: (Party) -> Unit) {
    if (parties == null) {
        return
    }

    val listOfParties = parties!!.map { party: Party -> @Composable{ PartyCard(party, onSetParty, onNavigate, onDeleteParty) } }
    PrettyList("Parties", true, "Create party", onButtonClick, listOfParties)
}

@Composable
fun FriendsList(friends: List<Account>?, onButtonClick: () -> Unit, onRemoveFriend: (Account) -> Unit) {
    if (friends == null) {
        return
    }
    val listOfFriends = friends!!.map { friend: Account -> @Composable{ FriendCard(friend, onRemoveFriend) } }
    PrettyList("Friends", true, "Add friend", onButtonClick, listOfFriends)
}