
package cl.duoc.proyectolince.ui.screens

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.proyectolince.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

// COLORES DE SOUTHBOUND SYNC
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
    val auth = remember {
        FirebaseAuth.getInstance()
    }

    var nombre by rememberSaveable {
        mutableStateOf("")
    }

    var correo by rememberSaveable {
        mutableStateOf("")
    }

    var contrasena by rememberSaveable {
        mutableStateOf("")
    }

    var confirmarContrasena by rememberSaveable {
        mutableStateOf("")
    }

    var crearCuenta by rememberSaveable {
        mutableStateOf(false)
    }

    var mostrarContrasena by remember {
        mutableStateOf(false)
    }

    var cargando by remember {
        mutableStateOf(false)
    }

    var mensaje by remember {
        mutableStateOf("")
    }

    var hayError by remember {
        mutableStateOf(false)
    }

    fun mostrarMensaje(
        texto: String,
        error: Boolean = true
    ) {
        mensaje = texto
        hayError = error
        cargando = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoSouthbound)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(12.dp))

        // LOGO ORIGINAL
        Image(
            painter = painterResource(
                id = R.drawable.logo_soutgbound
            ),
            contentDescription = "Logo Southbound Sync",
            modifier = Modifier.size(100.dp)
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
            text = "Logística turística inteligente y\n" +
                    "sincronización de rutas",
            textAlign = TextAlign.Center,
            color = TextoSouthbound,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

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
                    text = if (crearCuenta) {
                        "Crear cuenta"
                    } else {
                        "Bienvenido de nuevo"
                    },
                    color = TextoSouthbound,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = if (crearCuenta) {
                        "Regístrate con tus datos personales."
                    } else {
                        "Ingresa tu correo y contraseña."
                    },
                    color = TextoSouthbound,
                    style = MaterialTheme.typography.bodySmall
                )

                // NOMBRE SOLO AL REGISTRARSE
                if (crearCuenta) {
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = {
                            Text("Nombre completo")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !cargando,
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                // CORREO
                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    label = {
                        Text("Correo electrónico")
                    },
                    placeholder = {
                        Text("ejemplo@gmail.com")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = !cargando,
                    shape = RoundedCornerShape(12.dp)
                )

                // CONTRASEÑA
                OutlinedTextField(
                    value = contrasena,
                    onValueChange = { contrasena = it },
                    label = {
                        Text("Contraseña")
                    },
                    visualTransformation =
                        if (mostrarContrasena) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = !cargando,
                    shape = RoundedCornerShape(12.dp)
                )

                TextButton(
                    onClick = {
                        mostrarContrasena = !mostrarContrasena
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        text = if (mostrarContrasena) {
                            "Ocultar contraseña"
                        } else {
                            "Mostrar contraseña"
                        },
                        color = CelesteSouthbound
                    )
                }

                // CONFIRMAR CONTRASEÑA AL REGISTRARSE
                if (crearCuenta) {
                    OutlinedTextField(
                        value = confirmarContrasena,
                        onValueChange = {
                            confirmarContrasena = it
                        },
                        label = {
                            Text("Confirmar contraseña")
                        },
                        visualTransformation =
                            PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !cargando,
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                // BOTÓN PRINCIPAL
                Button(
                    onClick = {

                        val email = correo.trim()

                        if (!Patterns.EMAIL_ADDRESS
                                .matcher(email).matches()) {

                            mostrarMensaje(
                                "Ingresa un correo válido"
                            )

                        } else if (contrasena.isBlank()) {

                            mostrarMensaje(
                                "Ingresa una contraseña"
                            )

                        } else if (crearCuenta) {

                            // REGISTRAR USUARIO

                            if (nombre.isBlank()) {

                                mostrarMensaje(
                                    "Ingresa tu nombre completo"
                                )

                            } else if (contrasena.length < 8) {

                                mostrarMensaje(
                                    "La contraseña debe tener " +
                                            "al menos 8 caracteres"
                                )

                            } else if (
                                contrasena != confirmarContrasena
                            ) {

                                mostrarMensaje(
                                    "Las contraseñas no coinciden"
                                )

                            } else {

                                cargando = true
                                mensaje = ""

                                auth.createUserWithEmailAndPassword(
                                    email,
                                    contrasena
                                ).addOnCompleteListener { tarea ->

                                    if (tarea.isSuccessful) {

                                        val usuario = auth.currentUser

                                        if (usuario == null) {
                                            mostrarMensaje(
                                                "No se pudo obtener el usuario"
                                            )
                                        } else {

                                            val perfil =
                                                UserProfileChangeRequest
                                                    .Builder()
                                                    .setDisplayName(
                                                        nombre.trim()
                                                    )
                                                    .build()

                                            usuario.updateProfile(perfil)
                                                .addOnCompleteListener {

                                                    auth.setLanguageCode("es")

                                                    usuario.sendEmailVerification()
                                                        .addOnCompleteListener { envio ->

                                                            auth.signOut()

                                                            if (envio.isSuccessful) {
                                                                crearCuenta = false
                                                                contrasena = ""
                                                                confirmarContrasena = ""

                                                                mostrarMensaje(
                                                                    "Cuenta creada. " +
                                                                            "Revisa tu correo y " +
                                                                            "verifica tu cuenta.",
                                                                    false
                                                                )
                                                            } else {
                                                                mostrarMensaje(
                                                                    "La cuenta fue creada, " +
                                                                            "pero no se pudo enviar " +
                                                                            "la verificación."
                                                                )
                                                            }
                                                        }
                                                }
                                        }

                                    } else {

                                        mostrarMensaje(
                                            "No se pudo registrar la cuenta. " +
                                                    "Comprueba si el correo ya está registrado."
                                        )
                                    }
                                }
                            }

                        } else {

                            // INICIAR SESIÓN

                            cargando = true
                            mensaje = ""

                            auth.signInWithEmailAndPassword(
                                email,
                                contrasena
                            ).addOnCompleteListener { tarea ->

                                if (tarea.isSuccessful) {

                                    val usuario = auth.currentUser

                                    if (usuario?.isEmailVerified == true) {

                                        cargando = false
                                        mensaje = ""
                                        onLogin()

                                    } else {

                                        auth.signOut()

                                        mostrarMensaje(
                                            "Debes verificar tu correo " +
                                                    "antes de ingresar."
                                        )
                                    }

                                } else {

                                    mostrarMensaje(
                                        "Correo o contraseña incorrectos."
                                    )
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled = !cargando,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AzulSouthbound,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = if (crearCuenta) {
                            "Crear mi cuenta"
                        } else {
                            "Iniciar sesión"
                        },
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // RECUPERAR CONTRASEÑA
                if (!crearCuenta) {
                    TextButton(
                        onClick = {

                            val email = correo.trim()

                            if (!Patterns.EMAIL_ADDRESS
                                    .matcher(email).matches()) {

                                mostrarMensaje(
                                    "Primero escribe tu correo"
                                )

                            } else {

                                cargando = true

                                auth.setLanguageCode("es")

                                auth.sendPasswordResetEmail(email)
                                    .addOnCompleteListener {

                                        mostrarMensaje(
                                            "Si existe una cuenta, " +
                                                    "recibirás un correo para " +
                                                    "restablecer la contraseña.",
                                            false
                                        )
                                    }
                            }
                        },
                        enabled = !cargando,
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(
                            text = "¿Olvidaste tu contraseña?",
                            color = CelesteSouthbound
                        )
                    }
                }

                // ALTERNAR ENTRE LOGIN Y REGISTRO
                TextButton(
                    onClick = {
                        crearCuenta = !crearCuenta
                        contrasena = ""
                        confirmarContrasena = ""
                        mensaje = ""
                        hayError = false
                    },
                    enabled = !cargando,
                    modifier = Modifier.align(
                        Alignment.CenterHorizontally
                    )
                ) {
                    Text(
                        text = if (crearCuenta) {
                            "¿Ya tienes cuenta? Inicia sesión"
                        } else {
                            "¿No tienes cuenta? Regístrate"
                        },
                        color = AzulSouthbound
                    )
                }

                if (cargando) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(
                            Alignment.CenterHorizontally
                        ),
                        color = CelesteSouthbound
                    )
                }

                // MENSAJE DE FIREBASE
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
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // TARJETA INFERIOR
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

                Column(
                    modifier = Modifier.weight(1f)
                ) {
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
