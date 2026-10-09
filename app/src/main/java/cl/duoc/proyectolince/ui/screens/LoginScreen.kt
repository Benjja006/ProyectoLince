
package cl.duoc.proyectolince.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.proyectolince.R
import androidx.compose.material3.TextButton
private val AzulSouthbound = Color(0xFF122C5A)
private val CelesteSouthbound = Color(0xFF2CB6E8)
private val FondoSouthbound = Color(0xFFF5F7FC)
private val TextoSouthbound = Color(0xFF1E293B)
private val FondoCampo = Color(0xFFEEF3FF)

@Composable
fun LoginScreen(
    onLogin: () -> Unit
) {
    var usuario by rememberSaveable { mutableStateOf("") }
    var contraseña by rememberSaveable { mutableStateOf("") }
    var mostrarContraseña by remember { mutableStateOf(false) }

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
            painter = painterResource(id = R.drawable.logo_soutgbound),
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
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
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
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )

                Text(
                    text = "Bienvenido de nuevo",
                    color = TextoSouthbound,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Ingresa tus credenciales para continuar.",
                    color = TextoSouthbound,
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Usuario o correo",
                    color = TextoSouthbound,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.labelMedium
                )

                OutlinedTextField(
                    value = usuario,
                    onValueChange = { usuario = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text("Ingresa tu usuario")
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = FondoCampo,
                        focusedContainerColor = FondoCampo,
                        unfocusedBorderColor = FondoCampo,
                        focusedBorderColor = CelesteSouthbound,
                        cursorColor = AzulSouthbound
                    )
                )

                Text(
                    text = "Contraseña",
                    color = TextoSouthbound,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.labelMedium
                )

                OutlinedTextField(
                    value = contraseña,
                    onValueChange = { contraseña = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text("Ingresa tu contraseña")
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    visualTransformation = if (mostrarContraseña) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = FondoCampo,
                        focusedContainerColor = FondoCampo,
                        unfocusedBorderColor = FondoCampo,
                        focusedBorderColor = CelesteSouthbound,
                        cursorColor = AzulSouthbound
                    )
                )

                TextButton(
                    onClick = {
                        mostrarContraseña = !mostrarContraseña
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        text = if (mostrarContraseña) {
                            "Ocultar contraseña"
                        } else {
                            "Mostrar contraseña"
                        },
                        color = CelesteSouthbound
                    )
                }
                Button(
                    onClick = onLogin,
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
                        text = "Iniciar sesión",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Acceso al portal Southbound",
                        color = TextoSouthbound.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
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
    LoginScreen(onLogin = {})
}
