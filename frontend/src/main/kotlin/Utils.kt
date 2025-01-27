import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

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
                Spacer(modifier = androidx.compose.ui.Modifier.height(16.dp))
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
fun TextFieldDialog(question: String, onAccept: (String) -> Unit, onDismiss: () -> Unit, error: String? = null) {
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
                if (error != null) {
                    Text(error, color = MaterialTheme.colors.error)
                }
            }
        }
    }
}

@Composable
fun InfoDialog(text: String, onDismiss: () -> Unit) {
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
                Text(text = text)
                Button(onClick = onDismiss) {
                    Text("OK")
                }
            }
        }
    }
}
@Composable
fun ListHeader(name: String,
               showButton: Boolean,
               buttonDescription: String = "",
               onButtonClick: () -> Unit = {},
               buttonIcon: ImageVector) {
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
                    imageVector = buttonIcon,
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
fun PrettyList(name: String,
               showButton: Boolean,
               buttonDescription: String = "",
               onButtonClick: () -> Unit = {},
               content: List<@Composable () -> Unit>,
               buttonIcon: ImageVector = Icons.Default.Add) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.background, RoundedCornerShape(16.dp))
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        ListHeader(name, showButton, buttonDescription, onButtonClick, buttonIcon)
        PrettyListContent(content)
    }
}