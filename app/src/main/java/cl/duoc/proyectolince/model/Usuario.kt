package cl.duoc.proyectolince.model

data class Usuario(
    val id: Int,
    val nombre: String,
    val correo: String,
    val rol: Rol
)

enum class Rol{
    DRIVER,
    GUIA,
    COORDINADOR
}


//COMENTARIO DE Bnjja006: ESTA DATA CLASS ES UN OBJETO QUE DECLARA QUE DATOS VA A TENER UN USUARIO DE NUESTRA APP