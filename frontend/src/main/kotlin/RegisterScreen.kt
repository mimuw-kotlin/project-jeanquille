import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(onNavigate: (Screen) -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var phoneNumber by remember { mutableStateOf("") }
    var responseMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .background(color = MaterialTheme.colors.background)
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .padding(16.dp)
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Billance",
                fontSize = 60.sp
            )
            TextField(
                value = username,
                onValueChange = { newValue ->
                    if (validName(newValue))
                        username = newValue
                },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = password,
                onValueChange = { newValue ->
                    if (validName(newValue))
                        password = newValue
                },
                label = { Text("Password") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = {
                        passwordVisible = !passwordVisible
                    }) {
                        Icon(
                            imageVector = Icons.Default.Face,
                            contentDescription = "",
                            tint = MaterialTheme.colors.primary
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = phoneNumber,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() })
                        phoneNumber = newValue
                },
                label = { Text("Phone number") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                       responseMessage = registerUser(username, password, phoneNumber)
                    }
//                    onNavigate(Screen.Login)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create new account")
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (responseMessage.isNotEmpty()) {
                Text(responseMessage)
            }
        }
        Button(
            onClick = { onNavigate(Screen.Login) },
            modifier = Modifier.align(Alignment.BottomStart)
        ) {
            Text("Back to login screen")
        }
    }
}
