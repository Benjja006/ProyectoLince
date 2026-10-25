
package cl.duoc.proyectolince.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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

private val AzulServicios = Color(0xFF122C5A)
private val CelesteServicios = Color(0xFF2CB6E8)
private val FondoServicios = Color(0xFFF5F7FC)

data class ServicioFirebase(
    val id: String = "",
    val destino: String = "",
    val fecha: String = "",
    val hora: String = "",
    val tipo: String = "",
    val estado: String = ""
)

@Composable
fun ServiciosScreen(
    linceViewModel: LinceViewModel
) {
    val auth = remember {
        FirebaseAuth.getInstance()
    }

    val db = remember {
        FirebaseFirestore.getInstance()
    }

    val uid = auth.currentUser?.uid

    var servicios by remember {
        mutableStateOf(
            emptyList<ServicioFirebase>()
        )
    }

    var cargando by remember {
        mutableStateOf(true)
    }

    var error by remember {
        mutableStateOf("")
    }

    // Consultar los servicios del trabajador
    DisposableEffect(uid) {

        if (uid == null) {

            servicios = emptyList()
            cargando = false
            error = "No hay una sesión iniciada"

            onDispose { }

        } else {

            cargando = true
            servicios = emptyList()
            error = ""

            val listener = db.collection("servicios")
                .whereEqualTo("trabajadorUid", uid)
                .addSnapshotListener { resultado, excepcion ->

                    cargando = false

                    if (excepcion != null) {

                        error = "No se pudieron cargar los servicios"
                        servicios = emptyList()

                    } else {

                        error = ""

                        servicios = resultado?.documents
                            ?.map { documento ->

                                ServicioFirebase(
                                    id = documento.id,
                                    destino = documento.getString("destino")
                                        ?: "",
                                    fecha = documento.getString("fecha")
                                        ?: "",
                                    hora = documento.getString("hora")
                                        ?: "",
                                    tipo = documento.getString("tipo")
                                        ?: "",
                                    estado = documento.getString("estado")
                                        ?: ""
                                )
                            }
                            ?.sortedWith(
                                compareBy<ServicioFirebase> {
                                    it.fecha
                                }.thenBy {
                                    it.hora
                                }
                            )
                            ?: emptyList()
                    }
                }

            onDispose {
                listener.remove()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoServicios)
            .padding(20.dp)
    ) {

        Spacer(Modifier.height(25.dp))

        Text(
            text = "SOUTHBOUND SYNC",
            color = CelesteServicios,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(20.dp))

        Text(
            text = "Mis servicios",
            color = AzulServicios,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Servicios turísticos asignados a tu cuenta",
            color = Color.DarkGray
        )

        Spacer(Modifier.height(24.dp))

        if (cargando) {

            CircularProgressIndicator(
                color = CelesteServicios
            )

        } else if (error.isNotEmpty()) {

            Text(
                text = error,
                color = Color.Red
            )

        } else if (servicios.isEmpty()) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Text(
                    text = "Todavía no tienes servicios asignados.",
                    modifier = Modifier.padding(22.dp),
                    color = AzulServicios
                )
            }

        } else {

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(
                    bottom = 16.dp
                )
            ) {

                items(
                    items = servicios,
                    key = { it.id }
                ) { servicio ->

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
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement =
                                Arrangement.spacedBy(10.dp)
                        ) {

                            Text(
                                text = servicio.destino,
                                color = AzulServicios,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Fecha: ${servicio.fecha}"
                            )

                            Text(
                                text = "Hora: ${servicio.hora}"
                            )

                            Text(
                                text = "Tipo: ${servicio.tipo}"
                            )

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFFEEF3FF)
                            ) {

                                Text(
                                    text = servicio.estado,
                                    color = AzulServicios,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
