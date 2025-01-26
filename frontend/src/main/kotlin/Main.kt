import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

enum class Screen {
    Login,
    Home,
    Party,
    Register
}

@Composable
@Preview
fun App() {
    var currentScreen by remember { mutableStateOf(Screen.Login) }
    var currentUser by remember { mutableStateOf("") }
    var currentParty: Party? by remember { mutableStateOf(null) }

    MaterialTheme(
        colors = lightColors(
            primary = Color(0xFF6200EE),
            secondary = Color(0xFF03DAC6),
            secondaryVariant = Color(0xFF87CEEB),
            background = Color(0xFFBADBED),
        )
    ) {
        when (currentScreen) {
            Screen.Login -> LoginScreen(
                onNavigate = { screen -> currentScreen = screen },
                onSetUser = { user -> currentUser = user }
            )
            Screen.Home -> HomeScreen(
                userId = currentUser,
                onNavigate = { screen -> currentScreen = screen },
                onSetParty = { party -> currentParty = party}
            )
            Screen.Party -> PartyScreen(currentParty!!, currentUser) { screen -> currentScreen = screen }
            Screen.Register -> RegisterScreen { screen -> currentScreen = screen }
        }
    }
}



fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
