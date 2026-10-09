package cl.duoc.proyectolince.viewmodel

import androidx.lifecycle.ViewModel
import cl.duoc.proyectolince.model.EstadoServicio
import cl.duoc.proyectolince.model.Rol
import cl.duoc.proyectolince.model.Servicio
import cl.duoc.proyectolince.model.TipoServicio
import cl.duoc.proyectolince.model.Usuario

class LinceViewModel : ViewModel() {

    val usuarioActual = Usuario(
        id = 1,
        nombre = "Carlos Pérez",
        correo = "carlos@southbound.cl",
        rol = Rol.DRIVER
    )

    val servicios = listOf(
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
}

//COMENTARIO DE Benjja006: AQUI ESTAMSO CREANDO DATOS FICTICIOS PARA PROBAR LA APP ( SI ESTÁS VIENDO ESTE MENSAJE POR AHORA NO TENEMOS BASE DE DATOS)