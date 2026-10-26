package cl.duoc.proyectolince.model

data class Servicio(
    val id: Int,
    val fecha: String,
    val hora: String,
    val destino: String,
    val tipo: TipoServicio,
    val estado: EstadoServicio
)

enum class TipoServicio {
    FULL_DAY,
    HALF_DAY
}

enum class EstadoServicio {
    PENDIENTE,
    CONFIRMADO,
    RECHAZADO,
    FINALIZADO
}

//COMENTARIO DE Benjja006: ESTA DATA CLASS DECLARA QUE CONTENDRÁ CADA SERVICIO (TOUR, IDA A UN HOTEL, ETC) DE NUESTRA APP SIGUIENDO LA LOGICA DE NEGOCIO DE LOS TRABAJADORES