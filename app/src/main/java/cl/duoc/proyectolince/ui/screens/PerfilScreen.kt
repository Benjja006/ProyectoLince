
package cl.duoc.proyectolince.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.proyectolince.viewmodel.LinceViewModel

private val AzulPerfil = Color(0xFF122C5A)
private val CelestePerfil = Color(0xFF2CB6E8)
private val FondoPerfil = Color(0xFFF5F7FC)
private val TextoPerfil = Color(0xFF1E293B)

@Composable
fun PerfilScreen(
    linceViewModel: LinceViewModel = viewModel()
) {
    val usuario = linceViewModel.usuarioActual

    Scaffold(
        containerColor = FondoPerfil
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Text(
                text = "SOUTHBOUND SYNC",
                color = CelestePerfil,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Text(
                text = "Mi perfil",
                color = AzulPerfil,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Información de tu cuenta",
                color = TextoPerfil,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
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
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    Text(
                        text = "DATOS DEL USUARIO",
                        color = CelestePerfil,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            text = "Nombre",
                            color = TextoPerfil.copy(alpha = 0.7f),
                            fontSize = 12.sp
                        )

                        Text(
                            text = usuario.nombre,
                            color = AzulPerfil,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            text = "Correo electrónico",
                            color = TextoPerfil.copy(alpha = 0.7f),
                            fontSize = 12.sp
                        )

                        Text(
                            text = usuario.correo,
                            color = TextoPerfil,
                            fontSize = 15.sp
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            text = "Rol",
                            color = TextoPerfil.copy(alpha = 0.7f),
                            fontSize = 12.sp
                        )

                        Row(
                            modifier = Modifier
                                .background(
                                    color = Color(0xFFEEF3FF),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .padding(
                                    horizontal = 12.dp,
                                    vertical = 8.dp
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = when (usuario.rol.name) {
                                    "DRIVER" -> "Conductor"
                                    "GUIA" -> "Guía"
                                    "COORDINADOR" -> "Coordinador"
                                    else -> usuario.rol.name
                                },
                                color = AzulPerfil,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Southbound · Material Design 3",
                modifier = Modifier.fillMaxWidth(),
                color = TextoPerfil.copy(alpha = 0.65f),
                fontSize = 11.sp
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PerfilScreenPreview() {
    PerfilScreen()
}

