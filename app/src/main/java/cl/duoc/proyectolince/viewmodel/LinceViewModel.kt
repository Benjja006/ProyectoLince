
package cl.duoc.proyectolince.viewmodel

import androidx.lifecycle.ViewModel
import cl.duoc.proyectolince.model.EstadoServicio
import cl.duoc.proyectolince.model.LinceUiState
import cl.duoc.proyectolince.model.Rol
import cl.duoc.proyectolince.model.Servicio
import cl.duoc.proyectolince.model.TipoServicio
import cl.duoc.proyectolince.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LinceViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        LinceUiState(
            usuarioActual = Usuario(
                id = 1,
                nombre = "Carlos Pérez",
                correo = "carlos@southbound.cl",
                rol = Rol.DRIVER
            ),
            servicios = listOf(
                Servicio(
                    id = 1,
                    fecha = "08/10/2026",
                    hora = "09:00",
                    destino = "Santiago",
                    tipo = TipoServicio.FULL_DAY,
                    estado = EstadoServicio.CONFIRMADO
                ),
                Servicio(
                    id = 2,
                    fecha = "09/10/2026",
                    hora = "14:00",
                    destino = "Puerto Montt",
                    tipo = TipoServicio.HALF_DAY,
                    estado = EstadoServicio.PENDIENTE
                )
            )
        )
    )

    fun actualizarEstado(nuevoEstado: LinceUiState) {
        _uiState.value = nuevoEstado
    }

    val uiState = _uiState.asStateFlow()

    // Conservamos estas propiedades para no romper las pantallas actuales.
    val usuarioActual
        get() = uiState.value.usuarioActual!!

    val servicios
        get() = uiState.value.servicios
}
