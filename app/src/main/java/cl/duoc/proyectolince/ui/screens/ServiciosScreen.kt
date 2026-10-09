package cl.duoc.proyectolince.ui.screens
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import cl.duoc.proyectolince.model.Servicio
import cl.duoc.proyectolince.viewmodel.LinceViewModel

private val AzulServicios = Color(0xFF122C5A)
private val CelesteServicios = Color(0xFF2CB6E8)
private val FondoServicios = Color(0xFFF5F7FC)
private val TextoServicios = Color(0xFF1E293B)
private val VerdeServicios = Color(0xFF10B981)
private val RojoServicios = Color(0xFFEF4444)

@Composable
fun ServiciosScreen(
    linceViewModel: LinceViewModel = viewModel()
) {
    val uiState by linceViewModel.uiState.collectAsStateWithLifecycle()
    val servicios = uiState.servicios

    Scaffold(
        containerColor = FondoServicios
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "SOUTHBOUND SYNC",
                color = CelesteServicios,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Text(
                text = "Mis servicios",
                color = AzulServicios,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Consulta los detalles de tus servicios asignados.",
                color = TextoServicios,
                fontSize = 14.sp
            )

            if (servicios.isEmpty()) {
                Text(
                    text = "No hay servicios disponibles.",
                    color = TextoServicios
                )
            } else {
                servicios.forEach { servicio ->
                    ServicioCard(servicio = servicio)
                }
            }
        }
    }
}

@Composable
fun ServicioCard(servicio: Servicio) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "SERVICIO #${servicio.id}",
                color = CelesteServicios,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
            )

            Text(
                text = servicio.destino,
                color = AzulServicios,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Fecha: ${servicio.fecha}",
                        color = TextoServicios,
                        fontSize = 13.sp
                    )

                    Text(
                        text = "Hora: ${servicio.hora}",
                        color = TextoServicios,
                        fontSize = 13.sp
                    )
                }

                Text(
                    text = servicio.estado.name,
                    color = when (servicio.estado.name) {
                        "CONFIRMADO", "FINALIZADO" -> VerdeServicios
                        "RECHAZADO" -> RojoServicios
                        else -> AzulServicios
                    },
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.background(Color.Transparent).padding(0.dp))

            Text(
                text = when (servicio.tipo.name) {
                    "FULL_DAY" -> "Jornada completa"
                    "HALF_DAY" -> "Media jornada"
                    else -> servicio.tipo.name
                },
                color = TextoServicios.copy(alpha = 0.75f),
                fontSize = 13.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ServiciosScreenPreview() {
    ServicioCard(
        servicio = Servicio(
            id = 1,
            fecha = "08/10/2026",
            hora = "09:00",
            destino = "Santiago",
            tipo = cl.duoc.proyectolince.model.TipoServicio.FULL_DAY,
            estado = cl.duoc.proyectolince.model.EstadoServicio.CONFIRMADO
        )
    )
}
