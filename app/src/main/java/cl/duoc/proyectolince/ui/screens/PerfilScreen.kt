
package cl.duoc.proyectolince.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import cl.duoc.proyectolince.viewmodel.LinceViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private val AzulPerfil = Color(0xFF122C5A)
private val CelestePerfil = Color(0xFF2CB6E8)
private val FondoPerfil = Color(0xFFF5F7FC)
private val TextoPerfil = Color(0xFF1E293B)

@Composable
fun PerfilScreen(
    linceViewModel: LinceViewModel,
    onCerrarSesion: () -> Unit
) {
    val auth = remember {
        FirebaseAuth.getInstance()
    }

    val db = remember {
        FirebaseFirestore.getInstance()
    }

    var usuario by remember {
        mutableStateOf(auth.currentUser)
    }

    var rol by remember {
        mutableStateOf("Sin asignar")
    }

    // Detectar cambios de sesión
    DisposableEffect(auth) {

        val listener = FirebaseAuth.AuthStateListener {
            usuario = it.currentUser
        }

        auth.addAuthStateListener(listener)

        onDispose {
            auth.removeAuthStateListener(listener)
        }
    }

    val uid = usuario?.uid

    // Obtener rol del trabajador desde Firestore
    DisposableEffect(uid) {

        if (uid == null) {
            rol = "Sin asignar"

            onDispose { }

        } else {

            val listener = db.collection("usuarios")
                .document(uid)
                .addSnapshotListener { documento, error ->

                    rol = if (error == null &&
                        documento != null &&
                        documento.exists()
                    ) {
                        when (documento.getString("rol")) {
                            "conductor" -> "Conductor"
                            "guia" -> "Guía"
                            "coordinador" -> "Coordinador"
                            else -> "Sin asignar"
                        }
                    } else {
                        "Sin asignar"
                    }
                }

            onDispose {
                listener.remove()
            }
        }
    }

    val nombre = usuario?.displayName
        ?.takeIf { it.isNotBlank() }
        ?: "Nombre no configurado"

    val correo = usuario?.email
        ?: "Correo no disponible"

    val verificado = usuario?.isEmailVerified == true

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoPerfil)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        Spacer(Modifier.height(30.dp))

        Text(
            text = "SOUTHBOUND SYNC",
            color = CelestePerfil,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Mi perfil",
            color = AzulPerfil,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(10.dp))

        Text(
            text = "Información de tu cuenta",
            color = TextoPerfil,
            fontSize = 16.sp
        )

        Spacer(Modifier.height(35.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {

            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {

                Text(
                    text = "DATOS DEL USUARIO",
                    color = CelestePerfil,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )

                HorizontalDivider()

                Text(
                    text = "Nombre",
                    color = Color.Gray
                )

                Text(
                    text = nombre,
                    color = AzulPerfil,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Correo electrónico",
                    color = Color.Gray
                )

                Text(
                    text = correo,
                    color = TextoPerfil
                )

                Text(
                    text = "Rol",
                    color = Color.Gray
                )

                Surface(
                    color = Color(0xFFEEF3FF),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = rol,
                        color = AzulPerfil,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(12.dp)
                    )
                }

                HorizontalDivider()

                Text(
                    text = "Estado del correo",
                    color = Color.Gray
                )

                Text(
                    text = if (verificado) {
                        "Correo verificado"
                    } else {
                        "Correo no verificado"
                    },
                    color = if (verificado) {
                        Color(0xFF16803D)
                    } else {
                        Color(0xFFB45309)
                    },
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(Modifier.height(25.dp))

        // BOTÓN CERRAR SESIÓN
        Button(
            onClick = {
                auth.signOut()
                onCerrarSesion()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFB42318),
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Cerrar sesión",
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Southbound · Material Design 3",
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}
