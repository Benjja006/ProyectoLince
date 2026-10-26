package cl.duoc.proyectolince

import android.util.Patterns
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth

object CorreoAuth {

    private val auth = FirebaseAuth.getInstance()

    fun enviarEnlace(
        correo: String,
        resultado: (Boolean, String) -> Unit
    ) {

        val email = correo.trim()

        // Comprobar formato del correo
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            resultado(false, "Ingresa un correo válido")
            return
        }

        // Configurar el enlace de acceso
        val configuracion = ActionCodeSettings.newBuilder()
            .setUrl(
                "https://proyectolince-d9ad8.firebaseapp.com/finishSignIn"
            )
            .setHandleCodeInApp(true)
            .setAndroidPackageName(
                "cl.duoc.proyectolince",
                false,
                null
            )
            .build()

        // Solicitar el envío del correo
        auth.sendSignInLinkToEmail(
            email,
            configuracion
        ).addOnCompleteListener { tarea ->

            if (tarea.isSuccessful) {

                resultado(
                    true,
                    "Enlace solicitado. Revisa tu correo electrónico."
                )

            } else {

                resultado(
                    false,
                    tarea.exception?.localizedMessage
                        ?: "Error al enviar el correo"
                )
            }
        }
    }
}
