package cl.duoc.proyectolince

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cl.duoc.proyectolince.navigation.LinceNavigation
import cl.duoc.proyectolince.ui.theme.SouthboundSyncTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            SouthboundSyncTheme {

                LinceNavigation()

            }
        }
    }
}