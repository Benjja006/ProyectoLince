
package cl.duoc.proyectolince

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cl.duoc.proyectolince.navigation.LinceNavigation
import cl.duoc.proyectolince.ui.theme.SouthboundSyncTheme

class MainActivity : ComponentActivity() {

    private var enlaceRecibido by mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // Revisar si la aplicación se abrió desde un enlace
        enlaceRecibido = intent?.data?.toString()

        setContent {
            SouthboundSyncTheme {
                LinceNavigation(
                    enlaceRecibido = enlaceRecibido
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        setIntent(intent)

        // Recibir otro enlace mientras la aplicación está abierta
        enlaceRecibido = intent.data?.toString()
    }
}
