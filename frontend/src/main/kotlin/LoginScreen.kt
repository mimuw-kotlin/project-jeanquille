import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(onNavigate: (Screen) -> Unit, onSetUser: (String) -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var response: LoginResult? by remember { mutableStateOf(null) }
    var isLoading by remember { mutableStateOf(false) }

    val focusRequester1 = remember { FocusRequester() }
    val focusRequester2 = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .background(color = MaterialTheme.colors.background)
            .fillMaxSize()
            .padding(8.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        }
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
                modifier = Modifier
                    .focusRequester(focusRequester1)
                    .onKeyEvent { keyEvent ->
                        if (keyEvent.key == Key.Tab) {
                            coroutineScope.launch { focusRequester2.requestFocus() }
                            true // Consume the Tab key event
                        } else {
                            false
                        }
                    }
                    .fillMaxWidth(),
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
                modifier = Modifier
                    .focusRequester(focusRequester2)
                    .onKeyEvent { keyEvent ->
                        if (keyEvent.key == Key.Tab) {
                            coroutineScope.launch { focusRequester1.requestFocus() }
                            true // Consume the Tab key event
                        } else if (keyEvent.key == Key.Enter) {
                            isLoading = true
                            CoroutineScope(Dispatchers.IO).launch {
                                response = loginUser(username, password)
                            }
                            false
                        } else {
                            false
                        }
                    }
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    isLoading = true
                    CoroutineScope(Dispatchers.IO).launch {
                        response = loginUser(username, password)
                    }
                },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (response != null) {
                isLoading = false
                if (response!!.success) {
                    onSetUser(response!!.uuid)
                    onNavigate(Screen.Home)
                }
                else
                    Text("Error: ${response!!.errorMessage}")
            }
        }
        Button(
            onClick = { onNavigate(Screen.Register) },
            modifier = Modifier.align(Alignment.BottomStart)
        ) {
            Text("Create new account")
        }
    }
}
