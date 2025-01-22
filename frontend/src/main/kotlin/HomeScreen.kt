import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen() {
    Column {
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
                onClick = {},
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
                PartiesList()
            }
            Box(modifier = Modifier.weight(1f)) {
                PartiesList()
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
    Column {
        for (element in content) {
            Box(
                modifier = Modifier
                    .padding(8.dp)
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
fun PartiesList() {
    val listOfParties = listOf<@Composable () -> Unit>(
        { Text("Party1")},
        { Text("Party2")},
        { Text("Party3")}
    )
    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(MaterialTheme.colors.background, RoundedCornerShape(16.dp))
            .fillMaxWidth()
    ) {
        ListHeader("Parties", "Add party") {}
        PrettyListContent(listOfParties)
    }
}

@Composable
fun FriendsList() {
    val listOfFriends = listOf("Avamaco", "Żonkil", "Mati")
    Column {
        Row {
            Text("Friends")
            IconButton(
                onClick = {},
                modifier = Modifier.background(MaterialTheme.colors.secondary, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add friend",
                    tint = MaterialTheme.colors.primary
                )
            }
        }
        Column {
            for (friend in listOfFriends) {
                Text(friend)
            }
        }
    }
}