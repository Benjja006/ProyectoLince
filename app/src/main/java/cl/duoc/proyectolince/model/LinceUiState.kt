package cl.duoc.proyectolince.model

data class LinceUiState(
    val usuarioActual: Usuario? = null,
    val servicios: List<Servicio> = emptyList()
)
