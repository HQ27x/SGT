package com.example.srl

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class EditProductActivity : AppCompatActivity() {

    // Servicio de Firebase Firestore
    private lateinit var firestore: FirebaseFirestore

    // Elementos de la interfaz de usuario
    private lateinit var nameEditText: EditText
    private lateinit var categoryEditText: EditText
    private lateinit var quantityEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var productId: String

    /**
     * Esta función se llama cuando la actividad se crea por primera vez.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_product)

        // Inicializar Firestore
        firestore = FirebaseFirestore.getInstance()

        // Inicializar elementos de la interfaz de usuario
        nameEditText = findViewById(R.id.edit_text_name)
        categoryEditText = findViewById(R.id.edit_text_category)
        quantityEditText = findViewById(R.id.edit_text_quantity)
        priceEditText = findViewById(R.id.edit_text_price)
        val saveButton = findViewById<Button>(R.id.button_save)
        val cancelButton = findViewById<Button>(R.id.button_cancel)

        // Obtener los datos del producto a editar, que se pasan desde la actividad anterior
        productId = intent.getStringExtra("PRODUCT_ID") ?: ""
        val productName = intent.getStringExtra("PRODUCT_NAME") ?: ""
        val productCategory = intent.getStringExtra("PRODUCT_CATEGORY") ?: ""
        val productQuantity = intent.getIntExtra("PRODUCT_QUANTITY", 0)
        val productPrice = intent.getDoubleExtra("PRODUCT_PRICE", 0.0)

        // Rellenar los campos del formulario con los datos del producto
        nameEditText.setText(productName)
        categoryEditText.setText(productCategory)
        quantityEditText.setText(productQuantity.toString())
        priceEditText.setText(productPrice.toString())

        // Configurar el botón de guardar
        saveButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val category = categoryEditText.text.toString().trim()
            val quantity = quantityEditText.text.toString().toIntOrNull()
            val price = priceEditText.text.toString().toDoubleOrNull()

            // Validar que todos los campos estén rellenos
            if (name.isNotEmpty() && category.isNotEmpty() && quantity != null && price != null) {
                val updatedProduct = Product(name, category, quantity, price)
                updateProduct(updatedProduct)
            } else {
                Toast.makeText(this, "Por favor, rellena todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        // Configurar el botón de cancelar
        cancelButton.setOnClickListener {
            finish() // Cierra la actividad y vuelve a la anterior
        }
    }

    /**
     * Actualiza un producto existente en Firestore.
     */
    private fun updateProduct(product: Product) {
        firestore.collection("products").document(productId)
            .set(product)
            .addOnSuccessListener {
                val intent = Intent(this, EditSuccessActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al actualizar el producto: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}