package com.example.srl

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {

    // Servicio de autenticación de Firebase
    private lateinit var auth: FirebaseAuth

    // Elementos de la interfaz de usuario
    private lateinit var userNameTextView: TextView
    private lateinit var userEmailTextView: TextView
    private lateinit var changePasswordButton: Button
    private lateinit var logoutButton: Button
    private lateinit var toolbar: Toolbar

    /**
     * Esta función se llama cuando la actividad se crea por primera vez.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Inicializar elementos de la interfaz de usuario
        userNameTextView = findViewById(R.id.user_name_text)
        userEmailTextView = findViewById(R.id.user_email_text)
        changePasswordButton = findViewById(R.id.change_password_button)
        logoutButton = findViewById(R.id.logout_button)
        toolbar = findViewById(R.id.toolbar)

        // Configurar la Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Cargar y mostrar la información del usuario
        loadUserProfile()

        // Configurar el botón para cambiar la contraseña
        changePasswordButton.setOnClickListener {
            val user = auth.currentUser
            user?.email?.let {
                sendPasswordResetEmail(it)
            }
        }

        // Configurar el botón para cerrar sesión
        logoutButton.setOnClickListener {
            logoutUser()
        }
    }

    /**
     * Carga los datos del usuario actual y los muestra en la interfaz.
     */
    private fun loadUserProfile() {
        val user = auth.currentUser
        if (user != null) {
            // Mostrar nombre (si está disponible) o un placeholder
            val displayName = user.displayName
            if (!displayName.isNullOrEmpty()) {
                userNameTextView.text = displayName
            } else {
                // Si no hay nombre, se puede usar una parte del email o un genérico
                userNameTextView.text = user.email?.split("@")?.get(0) ?: "Usuario"
            }
            // Mostrar email
            userEmailTextView.text = user.email
        } else {
            // Si no hay usuario, volver a la pantalla de login
            goToLogin()
        }
    }

    /**
     * Envía un correo electrónico para restablecer la contraseña del usuario.
     */
    private fun sendPasswordResetEmail(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Se ha enviado un correo para restablecer tu contraseña.", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Error: No se pudo enviar el correo de restablecimiento.", Toast.LENGTH_LONG).show()
                }
            }
    }

    /**
     * Cierra la sesión del usuario actual y lo redirige a la pantalla de Login.
     */
    private fun logoutUser() {
        auth.signOut()
        goToLogin()
    }

    /**
     * Navega a la actividad de Login y limpia la pila de actividades.
     */
    private fun goToLogin() {
        val intent = Intent(this, Login::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    /**
     * Maneja el clic en el botón de "Atrás" de la toolbar.
     */
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}