
package cl.duoc.proyectolince.ui.screens

import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.lifecycle.viewmodel.compose.viewModel

import cl.duoc.proyectolince.viewmodel.LinceViewModel
import cl.duoc.proyectolince.viewmodel.ServiciosViewModel

import com.google.firebase.auth.FirebaseAuth

import java.util.Calendar
import java.util.Locale

private val AzulServicios = Color(0xFF122C5A)
private val CelesteServicios = Color(0xFF2CB6E8)
private val FondoServicios = Color(0xFFF5F7FC)

@Composable
fun ServiciosScreen(
    linceViewModel: LinceViewModel
) {

    // =====================================
    // VIEWMODEL, ROOM Y FIREBASE
    // =====================================

    val serviciosVM: ServiciosViewModel = viewModel()

    val servicios by serviciosVM.servicios.collectAsState()
    val cargando by serviciosVM.cargando.collectAsState()
    val error by serviciosVM.error.collectAsState()
    val conexion by serviciosVM.conexion.collectAsState()

    val context = LocalContext.current

    val uid = FirebaseAuth.getInstance()
        .currentUser?.uid

    // =====================================
    // BUSCADOR Y FILTROS
    // =====================================

    var busqueda by remember {
        mutableStateOf("")
    }

    var filtro by remember {
        mutableStateOf("TODOS")
    }

    var fechaSeleccionada by remember {
        mutableStateOf("")
    }

    // =====================================
    // CALENDARIO NATIVO
    // =====================================

    val abrirCalendario = {

        val calendario = Calendar.getInstance()

        DatePickerDialog(
            context,
            { _, anio, mes, dia ->

                fechaSeleccionada = String.format(
                    Locale.US,
                    "%04d-%02d-%02d",
                    anio,
                    mes + 1,
                    dia
                )
            },
            calendario.get(Calendar.YEAR),
            calendario.get(Calendar.MONTH),
            calendario.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    // =====================================
    // ALMACENAMIENTO DE COMPROBANTES
    // =====================================

    val preferencias = remember(uid) {
        context.getSharedPreferences(
            "comprobantes_${uid ?: "sin_sesion"}",
            Context.MODE_PRIVATE
        )
    }

    val nombres = remember(uid) {
        mutableStateMapOf<String, String>()
    }

    val uris = remember(uid) {
        mutableStateMapOf<String, String>()
    }

    var servicioSeleccionadoId by remember(uid) {
        mutableStateOf<String?>(null)
    }

    var mensajeDocumento by remember(uid) {
        mutableStateOf("")
    }

    // Recuperar comprobantes guardados al abrir la pantalla
    LaunchedEffect(uid, servicios) {

        nombres.clear()
        uris.clear()

        if (uid != null) {
            servicios.forEach { servicio ->

                val uriGuardada = preferencias.getString(
                    "uri_${servicio.id}",
                    null
                )

                val nombreGuardado = preferencias.getString(
                    "nombre_${servicio.id}",
                    null
                )

                if (
                    uriGuardada != null &&
                    nombreGuardado != null
                ) {
                    uris[servicio.id] = uriGuardada
                    nombres[servicio.id] = nombreGuardado
                }
            }
        }
    }

    // =====================================
    // SELECCIONAR DOCUMENTO NATIVO
    // =====================================

    val selectorDocumento =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.OpenDocument()
        ) { uri ->

            val servicioId = servicioSeleccionadoId

            if (
                uri != null &&
                servicioId != null &&
                uid != null
            ) {

                try {

                    // Mantener el permiso de lectura
                    context.contentResolver
                        .takePersistableUriPermission(
                            uri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )

                    var nombreArchivo = "Comprobante"

                    context.contentResolver.query(
                        uri,
                        arrayOf(OpenableColumns.DISPLAY_NAME),
                        null,
                        null,
                        null
                    )?.use { cursor ->

                        if (cursor.moveToFirst()) {
                            nombreArchivo = cursor.getString(0)
                                ?: nombreArchivo
                        }
                    }

                    // Guardar referencia local y nombre
                    preferencias.edit()
                        .putString(
                            "uri_$servicioId",
                            uri.toString()
                        )
                        .putString(
                            "nombre_$servicioId",
                            nombreArchivo
                        )
                        .apply()

                    nombres[servicioId] = nombreArchivo
                    uris[servicioId] = uri.toString()

                    mensajeDocumento =
                        "Comprobante guardado localmente"

                } catch (e: Exception) {

                    mensajeDocumento =
                        "No se pudo conservar el acceso al documento"
                }
            }

            servicioSeleccionadoId = null
        }

    // =====================================
    // ABRIR DOCUMENTO GUARDADO
    // =====================================

    fun abrirComprobante(
        servicioId: String
    ) {

        val uriTexto = uris[servicioId] ?: return

        try {

            val uri = Uri.parse(uriTexto)

            val tipo = context.contentResolver
                .getType(uri) ?: "*/*"

            val intent = Intent(
                Intent.ACTION_VIEW
            ).apply {

                setDataAndType(uri, tipo)

                addFlags(
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }

            context.startActivity(
                Intent.createChooser(
                    intent,
                    "Abrir comprobante"
                )
            )

        } catch (e: ActivityNotFoundException) {

            mensajeDocumento =
                "No hay una aplicación para abrir este archivo"

        } catch (e: Exception) {

            mensajeDocumento =
                "No se pudo abrir. Verifica que el archivo exista"
        }
    }

    // =====================================
    // FILTRAR SERVICIOS
    // =====================================

    val serviciosFiltrados = servicios.filter { servicio ->

        val coincideDestino = servicio.destino.contains(
            busqueda.trim(),
            ignoreCase = true
        )

        val coincideEstado =
            filtro == "TODOS" ||
                    servicio.estado == filtro

        val coincideFecha =
            fechaSeleccionada.isEmpty() ||
                    servicio.fecha == fechaSeleccionada

        coincideDestino &&
                coincideEstado &&
                coincideFecha
    }

    // =====================================
    // INTERFAZ
    // =====================================

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoServicios)
            .padding(20.dp)
    ) {

        Spacer(Modifier.height(15.dp))

        Text(
            text = "SOUTHBOUND SYNC",
            color = CelesteServicios,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(18.dp))

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

        Spacer(Modifier.height(6.dp))

        Text(
            text = conexion,
            color = Color.Gray,
            fontSize = 12.sp
        )

        Spacer(Modifier.height(18.dp))

        // BUSCADOR
        OutlinedTextField(
            value = busqueda,
            onValueChange = {
                busqueda = it
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Buscar por destino")
            },
            placeholder = {
                Text("Ej: Puerto Varas")
            },
            singleLine = true
        )

        Spacer(Modifier.height(12.dp))

        // CALENDARIO
        OutlinedButton(
            onClick = {
                abrirCalendario()
            },
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                if (fechaSeleccionada.isEmpty()) {
                    "Seleccionar fecha del servicio"
                } else {
                    "Fecha: $fechaSeleccionada"
                }
            )
        }

        if (fechaSeleccionada.isNotEmpty()) {

            TextButton(
                onClick = {
                    fechaSeleccionada = ""
                }
            ) {
                Text("Mostrar todas las fechas")
            }
        }

        Spacer(Modifier.height(8.dp))

        // FILTROS
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            listOf(
                "TODOS",
                "PENDIENTE",
                "CONFIRMADO",
                "FINALIZADO"
            ).forEach { estado ->

                FilterChip(
                    selected = filtro == estado,
                    onClick = {
                        filtro = estado
                    },
                    label = {
                        Text(
                            when (estado) {
                                "TODOS" -> "Todos"
                                "PENDIENTE" -> "Pendientes"
                                "CONFIRMADO" -> "Confirmados"
                                else -> "Finalizados"
                            },
                            fontSize = 12.sp
                        )
                    }
                )
            }
        }

        Spacer(Modifier.height(14.dp))

        // MENSAJES
        if (error.isNotEmpty()) {

            Text(
                text = error,
                color = Color(0xFFB45309),
                fontSize = 13.sp
            )

            Spacer(Modifier.height(8.dp))
        }

        if (mensajeDocumento.isNotEmpty()) {

            Text(
                text = mensajeDocumento,
                color = AzulServicios,
                fontSize = 12.sp
            )

            Spacer(Modifier.height(8.dp))
        }

        // =====================================
        // LISTA DE SERVICIOS
        // =====================================

        if (cargando) {

            CircularProgressIndicator(
                color = CelesteServicios
            )

        } else if (serviciosFiltrados.isEmpty()) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                Text(
                    text = if (
                        busqueda.isNotEmpty() ||
                        filtro != "TODOS" ||
                        fechaSeleccionada.isNotEmpty()
                    ) {
                        "No se encontraron servicios con esos filtros."
                    } else {
                        "Todavía no tienes servicios guardados."
                    },
                    modifier = Modifier.padding(22.dp),
                    color = AzulServicios
                )
            }

        } else {

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(
                    bottom = 90.dp
                )
            ) {

                items(
                    items = serviciosFiltrados,
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
                                text = "Tipo: " +
                                        when (servicio.tipo) {
                                            "FULL_DAY" -> "Jornada completa"
                                            "HALF_DAY" -> "Media jornada"
                                            else -> servicio.tipo
                                        }
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

                            HorizontalDivider(
                                color = Color(0xFFE5E7EB)
                            )

                            // SELECCIONAR COMPROBANTE
                            OutlinedButton(
                                onClick = {

                                    servicioSeleccionadoId = servicio.id

                                    mensajeDocumento = ""

                                    selectorDocumento.launch(
                                        arrayOf(
                                            "application/pdf",
                                            "image/*"
                                        )
                                    )
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Seleccionar comprobante")
                            }

                            // DOCUMENTO GUARDADO
                            nombres[servicio.id]?.let { nombre ->

                                Text(
                                    text = "Comprobante: $nombre",
                                    color = AzulServicios,
                                    fontSize = 12.sp
                                )

                                Button(
                                    onClick = {
                                        abrirComprobante(servicio.id)
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = AzulServicios
                                    )
                                ) {
                                    Text("Abrir comprobante")
                                }

                                TextButton(
                                    onClick = {

                                        preferencias.edit()
                                            .remove("uri_${servicio.id}")
                                            .remove("nombre_${servicio.id}")
                                            .apply()

                                        nombres.remove(servicio.id)
                                        uris.remove(servicio.id)

                                        mensajeDocumento =
                                            "Referencia local eliminada"
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Quitar comprobante")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
