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
    Party
}

@Composable
@Preview
fun App() {
    var currentScreen by remember { mutableStateOf(Screen.Login) }

    MaterialTheme(
        colors = lightColors(
            primary = Color(0xFF6200EE),
            secondary = Color(0xFF03DAC6),
            secondaryVariant = Color(0xFF87CEEB),
            background = Color(0xFFBADBED),
        )
    ) {
        when (currentScreen) {
            Screen.Login -> LoginScreen { screen -> currentScreen = screen }
            Screen.Home -> HomeScreen { screen -> currentScreen = screen }
            Screen.Party -> PartyScreen { screen -> currentScreen = screen }
        }
    }
}



fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
