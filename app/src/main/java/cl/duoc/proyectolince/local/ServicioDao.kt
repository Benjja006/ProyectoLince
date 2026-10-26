
package cl.duoc.proyectolince.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface ServicioDao {

    // Leer los servicios de un trabajador
    @Query("""
        SELECT * FROM servicios
        WHERE trabajadorUid = :uid
        ORDER BY fecha ASC, hora ASC
    """)
    fun obtenerServicios(
        uid: String
    ): Flow<List<ServicioEntity>>

    // Guardar o actualizar servicios
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarServicios(
        servicios: List<ServicioEntity>
    )

    // Buscar un servicio determinado
    @Query("""
        SELECT * FROM servicios
        WHERE id = :id
        AND trabajadorUid = :uid
        LIMIT 1
    """)
    suspend fun buscarServicio(
        id: String,
        uid: String
    ): ServicioEntity?

    // Borrar los servicios locales del trabajador
    @Query("""
        DELETE FROM servicios
        WHERE trabajadorUid = :uid
    """)
    suspend fun eliminarServicios(uid: String)

    // Actualizar todos sus servicios en una sola transacción
    @Transaction
    suspend fun reemplazarServicios(
        uid: String,
        servicios: List<ServicioEntity>
    ) {
        require(servicios.all { it.trabajadorUid == uid }) {
            "Los servicios deben pertenecer al trabajador"
        }

        eliminarServicios(uid)
        guardarServicios(servicios)
    }
}
