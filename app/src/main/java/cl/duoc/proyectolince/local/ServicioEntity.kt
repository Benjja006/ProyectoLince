
package cl.duoc.proyectolince.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "servicios")
data class ServicioEntity(

    @PrimaryKey
    val id: String,

    val trabajadorUid: String,

    val destino: String,

    val fecha: String,

    val hora: String,

    val tipo: String,

    val estado: String
)
