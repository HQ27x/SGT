package com.example.srl

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.srl.databinding.ActivityRegistroBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore // Para guardar datos adicionales
import android.util.Log // 🛑 ¡IMPORTACIÓN AÑADIDA!

class registro : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance() // Inicializa Firestore

        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.buttonRegistrar.setOnClickListener {
            performRegistro()
        }
    }

    private fun performRegistro() {
        val correo = binding.editTextCorreo.text.toString().trim()
        val password = binding.editTextPassword.text.toString()
        val confirmPassword = binding.editTextConfirmPassword.text.toString()

        if (correo.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            Toast.makeText(this, "Completa todos los campos.", Toast.LENGTH_SHORT).show()
            return
        }
        if (password != confirmPassword) {
            Toast.makeText(this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show()
            return
        }
        if (password.length < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres.", Toast.LENGTH_SHORT).show()
            return
        }

        // Llamada a Firebase Auth para crear el usuario
        auth.createUserWithEmailAndPassword(correo, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        saveNewUserData(userId, correo) // Guarda en Firestore
                    }
                    Toast.makeText(this, "Registro exitoso!", Toast.LENGTH_SHORT).show()

                    // Navegar al Dashboard (usando el nombre de clase 'dashboar')
                   // val intent = Intent(this, msreg::class.java)
                    val intent = Intent(this, msreg::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    // Manejar errores (ej: email ya en uso, contraseña inválida)
                    // 🛑 ¡LÍNEA AÑADIDA PARA LOGCAT!
                    val errorMessage = task.exception?.message
                    Log.e("REGISTRO_ERROR", "Firebase Error: $errorMessage")

                    Toast.makeText(this,
                        "Error al registrar: ${task.exception?.message}", // Muestra el mensaje de Firebase en el Toast
                        Toast.LENGTH_LONG).show()
                }
            }
    }

    // Función opcional para guardar datos iniciales en Firestore
    private fun saveNewUserData(userId: String, email: String) {
        val userMap = hashMapOf(
            "email" to email,
            "createdAt" to System.currentTimeMillis()
        )

        db.collection("users").document(userId)
            .set(userMap)
            .addOnFailureListener { e ->
                Toast.makeText(this, "Alerta: No se pudo guardar info en DB.", Toast.LENGTH_SHORT).show()
            }
    }
}