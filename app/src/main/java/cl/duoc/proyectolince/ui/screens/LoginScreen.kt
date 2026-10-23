
package cl.duoc.proyectolince.ui.screens

import android.content.Context
import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import cl.duoc.proyectolince.CorreoAuth
import cl.duoc.proyectolince.R
import com.google.firebase.auth.FirebaseAuth

private val AzulSouthbound = Color(0xFF122C5A)
private val CelesteSouthbound = Color(0xFF2CB6E8)
private val FondoSouthbound = Color(0xFFF5F7FC)
private val TextoSouthbound = Color(0xFF1E293B)
private val FondoCampo = Color(0xFFEEF3FF)

@Composable
fun LoginScreen(
    enlaceRecibido: String? = null,
    onLogin: () -> Unit
) {

    val context = LocalContext.current

    val auth = remember {
        FirebaseAuth.getInstance()
    }

    var correo by rememberSaveable {
        mutableStateOf("")
    }

    var mensaje by rememberSaveable {
        mutableStateOf("")
    }

    var hayError by remember {
        mutableStateOf(false)
    }

    var cargando by remember {
        mutableStateOf(false)
    }

    // Verificar el enlace recibido desde el correo
    LaunchedEffect(enlaceRecibido) {

        if (enlaceRecibido != null) {

            if (auth.isSignInWithEmailLink(enlaceRecibido)) {

                val preferencias = context.getSharedPreferences(
                    "southbound_login",
                    Context.MODE_PRIVATE
                )

                val correoGuardado = preferencias.getString(
                    "correo_pendiente",
                    null
                )

                if (correoGuardado != null) {

                    cargando = true
                    mensaje = "Verificando tu correo..."

                    auth.signInWithEmailLink(
                        correoGuardado,
                        enlaceRecibido
                    ).addOnCompleteListener { tarea ->

                        cargando = false

                        if (tarea.isSuccessful) {

                            preferencias.edit()
                                .remove("correo_pendiente")
                                .apply()

                            mensaje = "Inicio de sesión correcto"
                            hayError = false

                            // Entrar a la pantalla Inicio
                            onLogin()

                        } else {

                            mensaje =
                                "No se pudo verificar el enlace"
                            hayError = true
                        }
                    }

                } else {

                    mensaje =
                        "Solicita el enlace desde este dispositivo"
                    hayError = true
                }

            } else {

                mensaje = "El enlace recibido no es válido"
                hayError = true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoSouthbound)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(12.dp))

        Image(
            painter = painterResource(
                id = R.drawable.logo_soutgbound
            ),
            contentDescription = "Logo de Southbound Sync",
            modifier = Modifier.size(104.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Southbound Sync",
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold,
            color = AzulSouthbound
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Logística turística inteligente y\nsincronización de rutas",
            color = TextoSouthbound,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(26.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 3.dp
            )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    text = "PORTAL DE CONDUCTOR Y GUÍA",
                    color = CelesteSouthbound,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Bienvenido",
                    color = TextoSouthbound,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Ingresa tu correo para recibir un enlace de acceso.",
                    color = TextoSouthbound,
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Correo electrónico",
                    color = TextoSouthbound,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.labelMedium
                )

                OutlinedTextField(
                    value = correo,
                    onValueChange = {
                        correo = it
                        mensaje = ""
                        hayError = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text("ejemplo@gmail.com")
                    },
                    singleLine = true,
                    enabled = !cargando,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = FondoCampo,
                        focusedContainerColor = FondoCampo,
                        unfocusedBorderColor = FondoCampo,
                        focusedBorderColor = CelesteSouthbound,
                        cursorColor = AzulSouthbound
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                Button(
                    onClick = {

                        val email = correo.trim()

                        if (!Patterns.EMAIL_ADDRESS
                                .matcher(email).matches()) {

                            mensaje =
                                "Ingresa un correo electrónico válido"
                            hayError = true

                        } else {

                            cargando = true
                            hayError = false
                            mensaje = ""

                            CorreoAuth.enviarEnlace(email) {
                                    correcto, respuesta ->

                                cargando = false
                                mensaje = respuesta
                                hayError = !correcto

                                if (correcto) {

                                    val preferencias =
                                        context.getSharedPreferences(
                                            "southbound_login",
                                            Context.MODE_PRIVATE
                                        )

                                    preferencias.edit()
                                        .putString(
                                            "correo_pendiente",
                                            email
                                        )
                                        .apply()
                                }
                            }
                        }
                    },
                    enabled = !cargando,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AzulSouthbound,
                        contentColor = Color.White
                    )
                ) {

                    Text(
                        text = "Enviar enlace de acceso",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                if (cargando) {

                    CircularProgressIndicator(
                        color = CelesteSouthbound,
                        modifier = Modifier.align(
                            Alignment.CenterHorizontally
                        )
                    )
                }

                if (mensaje.isNotEmpty()) {

                    Text(
                        text = mensaje,
                        color = if (hayError) {
                            Color.Red
                        } else {
                            AzulSouthbound
                        },
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Text(
                    text = "Revisa tu correo y abre el enlace para iniciar sesión.",
                    color = TextoSouthbound.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = FondoCampo
            )
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(modifier = Modifier.weight(1f)) {

                    Text(
                        text = "SOUTHBOUND SYNC",
                        color = AzulSouthbound,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Gestión de servicios turísticos",
                        color = TextoSouthbound,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Text(
                    text = "●",
                    color = CelesteSouthbound,
                    fontSize = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Southbound · Material Design 3",
            color = TextoSouthbound.copy(alpha = 0.65f),
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {

    LoginScreen(
        enlaceRecibido = null,
        onLogin = {}
    )
}
