package com.example.srl

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore

class AddProductActivity : AppCompatActivity() {

    // Servicio de Firebase Firestore
    private lateinit var firestore: FirebaseFirestore

    // Elementos de la interfaz de usuario
    private lateinit var barcodeEditText: EditText
    private lateinit var scanBarcodeButton: ImageButton
    private lateinit var nameEditText: EditText
    private lateinit var categorySpinner: Spinner
    private lateinit var quantityEditText: EditText
    private lateinit var priceEditText: EditText

    // Lista de datos
    private val categoryList = mutableListOf<String>()

    // Lanzador para la actividad de escaneo de código de barras
    private val scanBarcodeLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val scannedBarcode = result.data?.getStringExtra("SCANNED_BARCODE")
            if (!scannedBarcode.isNullOrEmpty()) {
                barcodeEditText.setText(scannedBarcode)
                Toast.makeText(this, "Código escaneado: $scannedBarcode", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Esta función se llama cuando la actividad se crea por primera vez.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        // Inicializar Firestore
        firestore = FirebaseFirestore.getInstance()

        // Inicializar elementos de la interfaz de usuario
        barcodeEditText = findViewById(R.id.edit_text_barcode)
        scanBarcodeButton = findViewById(R.id.scan_barcode_button)
        nameEditText = findViewById(R.id.edit_text_name)
        categorySpinner = findViewById(R.id.category_spinner)
        quantityEditText = findViewById(R.id.edit_text_quantity)
        priceEditText = findViewById(R.id.edit_text_price)
        val saveButton = findViewById<Button>(R.id.button_save)
        val cancelButton = findViewById<Button>(R.id.button_cancel)

        // Configurar el botón de escaneo de código de barras
        scanBarcodeButton.setOnClickListener {
            val intent = Intent(this, ScanBarcodeActivity::class.java)
            scanBarcodeLauncher.launch(intent)
        }

        // Configurar el Spinner de categorías
        setupCategorySpinner()

        // Configurar el botón de guardar
        saveButton.setOnClickListener {
            val barcode = barcodeEditText.text.toString().trim()
            val name = nameEditText.text.toString().trim()
            val category = categorySpinner.selectedItem?.toString() ?: ""
            val quantity = quantityEditText.text.toString().toIntOrNull()
            val price = priceEditText.text.toString().toDoubleOrNull()

            // Validar que todos los campos obligatorios estén rellenos
            if (name.isNotEmpty() && category.isNotEmpty() && quantity != null && price != null) {
                // El código de barras es opcional, pero si se escanea se guarda
                val product = Product(name, category, quantity, price, barcode)
                saveProduct(product)
            } else {
                Toast.makeText(this, "Por favor, rellena todos los campos obligatorios", Toast.LENGTH_SHORT).show()
            }
        }

        // Configurar el botón de cancelar
        cancelButton.setOnClickListener {
            finish() // Cierra la actividad y vuelve a la anterior
        }

        // Configurar la navegación inferior
        setupBottomNavigation()
    }

    /**
     * Obtiene la lista de categorías de Firestore y las muestra en el Spinner.
     */
    private fun setupCategorySpinner() {
        firestore.collection("categories")
            .get()
            .addOnSuccessListener { snapshots ->
                categoryList.clear()
                for (document in snapshots) {
                    val category = document.toObject(Category::class.java)
                    categoryList.add(category.name)
                }
                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryList)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                categorySpinner.adapter = adapter
            }
            .addOnFailureListener { e ->
                Log.w("AddProductActivity", "Error al obtener las categorías", e)
            }
    }

    /**
     * Guarda un nuevo producto en Firestore.
     */
    private fun saveProduct(product: Product) {
        firestore.collection("products")
            .add(product)
            .addOnSuccessListener {
                val intent = Intent(this, ProductRegisteredActivity::class.java)
                startActivity(intent)
                clearForm()
            }
            .addOnFailureListener { e ->
                Log.e("AddProductActivity", "Error al guardar el producto en Firestore", e)
                Toast.makeText(this, "Error al guardar el producto: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    /**
     * Limpia los campos del formulario después de guardar un producto.
     */
    private fun clearForm() {
        barcodeEditText.text.clear()
        nameEditText.text.clear()
        quantityEditText.text.clear()
        priceEditText.text.clear()
        // Opcional: resetear el spinner a la primera posición si se desea
    }

    /**
     * Configura la vista de navegación inferior y su listener de selección de elementos.
     */
    private fun setupBottomNavigation() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this, DashboardActivity::class.java))
                    true
                }
                R.id.navigation_search -> {
                    Toast.makeText(this, "Función de búsqueda próximamente", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.navigation_add -> {
                    // Ya estamos en la pantalla de añadir producto, no hacer nada
                    true
                }
                else -> false
            }
        }
    }
}