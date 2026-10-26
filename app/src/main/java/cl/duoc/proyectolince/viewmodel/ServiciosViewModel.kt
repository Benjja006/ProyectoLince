
package cl.duoc.proyectolince.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope

import cl.duoc.proyectolince.local.LinceDatabase
import cl.duoc.proyectolince.local.ServicioEntity
import cl.duoc.proyectolince.repository.ServicioRepository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ServiciosViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val auth = FirebaseAuth.getInstance()

    private val repository = ServicioRepository(
        servicioDao = LinceDatabase
            .obtenerDatabase(application)
            .servicioDao(),
        firestore = FirebaseFirestore.getInstance()
    )

    private val _servicios =
        MutableStateFlow<List<ServicioEntity>>(emptyList())
    val servicios = _servicios.asStateFlow()

    private val _cargando = MutableStateFlow(true)
    val cargando = _cargando.asStateFlow()

    private val _error = MutableStateFlow("")
    val error = _error.asStateFlow()

    private val _conexion =
        MutableStateFlow("Comprobando servicios...")
    val conexion = _conexion.asStateFlow()

    private var uidActual: String? = null
    private var trabajoLocal: Job? = null
    private var trabajoRemoto: Job? = null

    // Escuchar cambios de sesión
    private val authListener =
        FirebaseAuth.AuthStateListener { firebaseAuth ->

            val nuevoUid = firebaseAuth.currentUser?.uid

            if (nuevoUid != uidActual) {
                cambiarUsuario(nuevoUid)
            }
        }

    init {
        auth.addAuthStateListener(authListener)
    }

    private fun cambiarUsuario(nuevoUid: String?) {

        // Detener las consultas del usuario anterior
        trabajoLocal?.cancel()
        trabajoRemoto?.cancel()

        uidActual = nuevoUid

        // Limpiar el estado mostrado en pantalla
        _servicios.value = emptyList()
        _error.value = ""
        _cargando.value = nuevoUid != null

        if (nuevoUid == null) {
            _conexion.value = "Sesión no iniciada"
            _cargando.value = false
            return
        }

        _conexion.value = "Cargando servicios locales"

        observarServiciosLocales(nuevoUid)
        sincronizarConFirebase(nuevoUid)
    }

    private fun observarServiciosLocales(uid: String) {

        trabajoLocal = viewModelScope.launch {

            try {

                repository.obtenerServiciosLocales(uid)
                    .collect { lista ->

                        if (uid == uidActual) {
                            _servicios.value = lista
                            _cargando.value = false
                        }
                    }

            } catch (e: Exception) {

                if (
                    e is kotlinx.coroutines.CancellationException
                ) {
                    throw e
                }

                if (uid == uidActual) {
                    _error.value =
                        "No se pudieron leer los datos locales"
                    _cargando.value = false
                }
            }
        }
    }

    private fun sincronizarConFirebase(uid: String) {

        trabajoRemoto = viewModelScope.launch {

            repository.observarServiciosRemotos(uid)
                .collect { resultado ->

                    if (uid != uidActual) {
                        return@collect
                    }

                    resultado.onSuccess { respuesta ->

                        if (respuesta.confirmadoPorServidor) {

                            try {

                                repository.reemplazarServiciosLocales(
                                    uid = uid,
                                    servicios = respuesta.servicios
                                )

                                if (uid == uidActual) {
                                    _conexion.value =
                                        "Sincronizado con Firebase"
                                    _error.value = ""
                                }

                            } catch (e: Exception) {

                                if (
                                    e is kotlinx.coroutines.CancellationException
                                ) {
                                    throw e
                                }

                                if (uid == uidActual) {
                                    _error.value =
                                        "No se pudo actualizar Room"
                                }
                            }

                        } else {

                            if (uid == uidActual) {
                                _conexion.value =
                                    "Mostrando servicios guardados"
                            }
                        }
                    }

                    resultado.onFailure {

                        if (uid == uidActual) {
                            _conexion.value =
                                "Mostrando servicios guardados"

                            _error.value =
                                "No se pudo conectar con Firebase"
                        }
                    }
                }
        }
    }

    override fun onCleared() {

        auth.removeAuthStateListener(authListener)

        trabajoLocal?.cancel()
        trabajoRemoto?.cancel()

        super.onCleared()
    }
}
