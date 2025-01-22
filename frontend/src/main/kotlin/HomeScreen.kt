import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun HomeScreen() {
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showAddPartyDialog by remember { mutableStateOf(false) }
    var showAddFriendDialog by remember { mutableStateOf(false) }

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
                    text = "Avamaco",
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
                    PartiesList({ showAddPartyDialog = true })
                }
                Box(modifier = Modifier.weight(1f)) {
                    FriendsList({ showAddFriendDialog = true })
                }
            }
        }
        if (showLogoutDialog) {
            LogoutDialog(onDismiss = { showLogoutDialog = false })
        }
        if (showAddPartyDialog) {
            TextFieldDialog(
                question = "Enter party name",
                onAccept = { showAddPartyDialog = false },
                onDismiss = { showAddPartyDialog = false }
            )
        }
        if (showAddFriendDialog) {
            TextFieldDialog(
                question = "Enter friend name",
                onAccept = { showAddFriendDialog = false },
                onDismiss = { showAddFriendDialog = false }
            )
        }
    }
}

@Composable
fun LogoutDialog(onDismiss: () -> Unit) {
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
                Text(text = "Do you really want to log out?")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onDismiss) {
                    Text("Log out")
                }
                Button(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        }
    }
}

@Composable
fun TextFieldDialog(question: String, onAccept: () -> Unit, onDismiss: () -> Unit) {
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
                Button(onClick = onAccept) {
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
fun ListHeader(name: String, buttonDescription: String, onButtonClick: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(16.dp).fillMaxWidth()
    ) {
        Text(
            text = name,
            fontSize = 24.sp
        )
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

@Composable
fun ListHeader(name: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(16.dp).fillMaxWidth()
    ) {
        Text(
            text = name,
            fontSize = 24.sp
        )
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
fun PartiesList(onButtonClick: () -> Unit) {
    val listOfParties = listOf<@Composable () -> Unit>(
        { Text("Party1")},
        { Text("Party2")},
        { Text("Party3")}
    )
    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.background, RoundedCornerShape(16.dp))
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        ListHeader("Parties", "Add party", onButtonClick)
        PrettyListContent(listOfParties)
    }
}

@Composable
fun FriendsList(onButtonClick: () -> Unit) {
    val listOfFriends = listOf<@Composable () -> Unit>(
        { Text("Żonkil")},
        { Text("Mati")},
        { Text("Kwiatek")}
    )
    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.background, RoundedCornerShape(16.dp))
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        ListHeader("Friends", "Add friend", onButtonClick)
        PrettyListContent(listOfFriends)
    }
}