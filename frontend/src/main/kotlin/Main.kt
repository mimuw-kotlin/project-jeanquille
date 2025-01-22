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

@Composable
@Preview
fun App() {
//    var text by remember { mutableStateOf("Hello, World!") }

    MaterialTheme(
        colors = lightColors(
            primary = Color(0xFF6200EE),
            secondary = Color(0xFF03DAC6),
            secondaryVariant = Color(0xFF87CEEB),
            background = Color(0xFFBADBED),
        )
    ) {
        HomeScreen()
    }
}



fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
