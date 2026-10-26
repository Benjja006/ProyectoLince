
package cl.duoc.proyectolince.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

private val AzulLince = Color(0xFF122C5A)
private val CelesteLince = Color(0xFF2CB6E8)

@Composable
fun LinceBottomBar(navController: NavController) {

    val backStackEntry = navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry.value?.destination?.route

    NavigationBar(
        containerColor = Color.White
    ) {
        NavigationBarItem(
            selected = rutaActual == "inicio",
            onClick = {
                navController.navigate("inicio") {
                    popUpTo("inicio") {
                        inclusive = false
                    }
                    launchSingleTop = true
                }
            },
            icon = {
                Text("⌂", fontSize = 22.sp)
            },
            label = {
                Text("Inicio")
            },
            colors = androidx.compose.material3.NavigationBarItemDefaults.colors(
                selectedIconColor = AzulLince,
                selectedTextColor = AzulLince,
                indicatorColor = CelesteLince
            )
        )

        NavigationBarItem(
            selected = rutaActual == "servicios",
            onClick = {
                navController.navigate("servicios") {
                    popUpTo("inicio") {
                        inclusive = false
                    }
                    launchSingleTop = true
                }
            },
            icon = {
                Text("▣", fontSize = 22.sp)
            },
            label = {
                Text("Servicios")
            },
            colors = androidx.compose.material3.NavigationBarItemDefaults.colors(
                selectedIconColor = AzulLince,
                selectedTextColor = AzulLince,
                indicatorColor = CelesteLince
            )
        )

        NavigationBarItem(
            selected = rutaActual == "perfil",
            onClick = {
                navController.navigate("perfil") {
                    popUpTo("inicio") {
                        inclusive = false
                    }
                    launchSingleTop = true
                }
            },
            icon = {
                Text("●", fontSize = 22.sp)
            },
            label = {
                Text("Perfil")
            },
            colors = androidx.compose.material3.NavigationBarItemDefaults.colors(
                selectedIconColor = AzulLince,
                selectedTextColor = AzulLince,
                indicatorColor = CelesteLince
            )
        )
    }
}
