package com.example.srl

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {

    // Servicio de autenticación de Firebase
    private lateinit var auth: FirebaseAuth

    /**
     * Esta función se llama cuando la actividad se crea por primera vez.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Inicializar elementos de la interfaz de usuario
        val emailTextView = findViewById<TextView>(R.id.email_text_view)
        val changePasswordButton = findViewById<Button>(R.id.change_password_button)

        // Obtener el usuario actual y mostrar su correo electrónico
        val user = auth.currentUser
        emailTextView.text = "Correo: ${user?.email}"

        // Configurar el botón para cambiar la contraseña
        changePasswordButton.setOnClickListener {
            user?.email?.let {
                sendPasswordResetEmail(it)
            }
        }
    }

    /**
     * Envía un correo electrónico para restablecer la contraseña del usuario.
     */
    private fun sendPasswordResetEmail(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Correo de restablecimiento enviado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al enviar el correo", Toast.LENGTH_SHORT).show()
                }
            }
    }
}