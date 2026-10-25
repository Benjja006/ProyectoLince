
package cl.duoc.proyectolince.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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

import com.google.firebase.auth.FirebaseAuth

@Composable
fun LinceNavigation(
    enlaceRecibido: String? = null
) {
    val auth = remember {
        FirebaseAuth.getInstance()
    }

    val navController = rememberNavController()

    val linceViewModel: LinceViewModel = viewModel()

    var sesionIniciada by remember {
        mutableStateOf(
            auth.currentUser?.isEmailVerified == true
        )
    }

    // Observar cambios de sesión
    DisposableEffect(auth) {

        val listener = FirebaseAuth.AuthStateListener {
                firebase ->

            sesionIniciada =
                firebase.currentUser?.isEmailVerified == true
        }

        auth.addAuthStateListener(listener)

        onDispose {
            auth.removeAuthStateListener(listener)
        }
    }

    val backStackEntry by
    navController.currentBackStackEntryAsState()

    val rutaActual = backStackEntry?.destination?.route

    // Recordar la sesión al abrir la aplicación
    val destinoInicial = remember {
        if (auth.currentUser?.isEmailVerified == true) {
            "inicio"
        } else {
            "login"
        }
    }

    // Impedir permanecer en pantallas protegidas
    // después de cerrar la sesión
    LaunchedEffect(sesionIniciada, rutaActual) {

        if (
            !sesionIniciada &&
            rutaActual != null &&
            rutaActual != "login"
        ) {
            navController.navigate("login") {

                popUpTo(navController.graph.id) {
                    inclusive = true
                }

                launchSingleTop = true
            }
        }
    }

    Scaffold(
        bottomBar = {

            if (
                sesionIniciada &&
                rutaActual in listOf(
                    "inicio",
                    "servicios",
                    "perfil"
                )
            ) {
                LinceBottomBar(
                    navController = navController
                )
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = destinoInicial,
            modifier = Modifier.padding(innerPadding)
        ) {

            // LOGIN
            composable("login") {

                LoginScreen(
                    enlaceRecibido = enlaceRecibido,

                    onLogin = {

                        sesionIniciada = true

                        navController.navigate("inicio") {

                            popUpTo("login") {
                                inclusive = true
                            }

                            launchSingleTop = true
                        }
                    }
                )
            }

            // INICIO
            composable("inicio") {

                if (sesionIniciada) {

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
            }

            // SERVICIOS
            composable("servicios") {

                if (sesionIniciada) {

                    ServiciosScreen(
                        linceViewModel = linceViewModel
                    )
                }
            }

            // PERFIL
            composable("perfil") {

                if (sesionIniciada) {

                    PerfilScreen(
                        linceViewModel = linceViewModel,

                        onCerrarSesion = {

                            sesionIniciada = false

                            navController.navigate("login") {

                                popUpTo(navController.graph.id) {
                                    inclusive = true
                                }

                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        }
    }
}
