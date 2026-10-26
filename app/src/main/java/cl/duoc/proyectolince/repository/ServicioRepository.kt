
package cl.duoc.proyectolince.repository

import cl.duoc.proyectolince.local.ServicioDao
import cl.duoc.proyectolince.local.ServicioEntity

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

// Resultado de una consulta a Firebase
data class ServiciosRemotos(
    val servicios: List<ServicioEntity>,
    val confirmadoPorServidor: Boolean
)

class ServicioRepository(
    private val servicioDao: ServicioDao,
    private val firestore: FirebaseFirestore
) {

    // Leer los servicios del teléfono
    fun obtenerServiciosLocales(
        uid: String
    ): Flow<List<ServicioEntity>> {
        return servicioDao.obtenerServicios(uid)
    }

    // Escuchar los cambios de Firebase
    fun observarServiciosRemotos(
        uid: String
    ): Flow<Result<ServiciosRemotos>> = callbackFlow {

        val listener = firestore
            .collection("servicios")
            .whereEqualTo("trabajadorUid", uid)
            .addSnapshotListener(
                MetadataChanges.INCLUDE
            ) { resultado, error ->

                if (error != null) {

                    trySend(Result.failure(error))

                } else if (resultado != null) {

                    val lista = resultado.documents.map { documento ->

                        ServicioEntity(
                            id = documento.id,
                            trabajadorUid = uid,
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

                    trySend(
                        Result.success(
                            ServiciosRemotos(
                                servicios = lista,
                                confirmadoPorServidor =
                                    !resultado.metadata.isFromCache
                            )
                        )
                    )
                }
            }

        awaitClose {
            listener.remove()
        }
    }

    // Reemplazar servicios tras confirmación del servidor
    suspend fun reemplazarServiciosLocales(
        uid: String,
        servicios: List<ServicioEntity>
    ) {
        servicioDao.reemplazarServicios(
            uid = uid,
            servicios = servicios
        )
    }

    // Buscar un servicio en el teléfono
    suspend fun buscarServicioLocal(
        id: String,
        uid: String
    ): ServicioEntity? {
        return servicioDao.buscarServicio(id, uid)
    }
}
