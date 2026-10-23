
package cl.duoc.proyectolince.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

import cl.duoc.proyectolince.viewmodel.LinceViewModel
import cl.duoc.proyectolince.ui.components.LinceBottomBar
import cl.duoc.proyectolince.ui.screens.InicioScreen
import cl.duoc.proyectolince.ui.screens.LoginScreen
import cl.duoc.proyectolince.ui.screens.PerfilScreen
import cl.duoc.proyectolince.ui.screens.ServiciosScreen

@Composable
fun LinceNavigation(
    enlaceRecibido: String? = null
) {

    val navController = rememberNavController()

    val linceViewModel: LinceViewModel = viewModel()

    val backStackEntry by
    navController.currentBackStackEntryAsState()

    val rutaActual = backStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (
                rutaActual == "inicio" ||
                rutaActual == "servicios" ||
                rutaActual == "perfil"
            ) {
                LinceBottomBar(
                    navController = navController
                )
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = androidx.compose.ui.Modifier.padding(
                innerPadding
            )
        ) {

            // PANTALLA LOGIN
            composable("login") {

                LoginScreen(
                    enlaceRecibido = enlaceRecibido,

                    onLogin = {

                        navController.navigate("inicio") {

                            popUpTo("login") {
                                inclusive = true
                            }

                            launchSingleTop = true
                        }
                    }
                )
            }

            // PANTALLA INICIO
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

            // PANTALLA SERVICIOS
            composable("servicios") {

                ServiciosScreen(
                    linceViewModel = linceViewModel
                )
            }

            // PANTALLA PERFIL
            composable("perfil") {

                PerfilScreen(
                    linceViewModel = linceViewModel
                )
            }
        }
    }
}
