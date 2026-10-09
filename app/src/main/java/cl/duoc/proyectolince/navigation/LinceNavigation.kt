
package cl.duoc.proyectolince.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cl.duoc.proyectolince.ui.components.LinceBottomBar
import cl.duoc.proyectolince.ui.screens.InicioScreen
import cl.duoc.proyectolince.ui.screens.LoginScreen
import cl.duoc.proyectolince.ui.screens.PerfilScreen
import cl.duoc.proyectolince.ui.screens.ServiciosScreen

@Composable
fun LinceNavigation() {
    val navController = rememberNavController()

    val backStackEntry = navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry.value?.destination?.route

    Scaffold(
        bottomBar = {
            if (rutaActual == "inicio" ||
                rutaActual == "servicios" ||
                rutaActual == "perfil"
            ) {
                LinceBottomBar(navController = navController)
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("login") {
                LoginScreen(
                    onLogin = {
                        navController.navigate("inicio") {
                            popUpTo("login") {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable("inicio") {
                InicioScreen(
                    onServiciosClick = {
                        navController.navigate("servicios") {
                            launchSingleTop = true
                        }
                    },
                    onPerfilClick = {
                        navController.navigate("perfil") {
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable("servicios") {
                ServiciosScreen()
            }

            composable("perfil") {
                PerfilScreen()
            }
        }
    }
}
