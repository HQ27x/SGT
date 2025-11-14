package com.example.srl

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
// Importa el Binding generado para ActivityLoginBinding
import com.example.srl.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    // Declaración del binding y Firebase Auth
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Inicialización de Firebase Auth
        auth = FirebaseAuth.getInstance()

        // 2. Verificar si el usuario ya está logueado (sesión persistente)
        if (auth.currentUser != null) {
            // Si hay un usuario logueado, ir directamente al Dashboard
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
            return
        }

        // 3. Inflar el layout (ActivityLoginBinding)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        // 4. Configurar Listeners
        setupListeners()
    }

    private fun setupListeners() {
        // Listener para el botón de Iniciar Sesión
        binding.buttonContinuar.setOnClickListener {
            performLogin()
        }

        // Listener para el enlace "No tienes una cuenta"
        // Asumiendo que tu Activity de registro se llama 'registro'
        binding.textRegisterLink.setOnClickListener {
            startActivity(Intent(this, registro::class.java))
        }
    }

    private fun performLogin() {
        val correo = binding.editTextCorreo.text.toString().trim()
        val password = binding.editTextPassword.text.toString()

        if (correo.isBlank() || password.isBlank()) {
            Toast.makeText(this, "Introduce correo y contraseña.", Toast.LENGTH_SHORT).show()
            return
        }

        // 5. Llamada a Firebase Auth para iniciar sesión
        auth.signInWithEmailAndPassword(correo, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "¡Inicio de sesión exitoso!", Toast.LENGTH_SHORT).show()

                    // Navegar al Dashboard
                    val intent = Intent(this, SuccessfulLoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Manejar error (ej: credenciales incorrectas)
                    Toast.makeText(this,
                        "Error de inicio de sesión: ${task.exception?.localizedMessage}",
                        Toast.LENGTH_LONG).show()
                }
            }
    }
}