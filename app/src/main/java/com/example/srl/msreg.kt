package com.example.srl

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
// Nota: 'ContentRegistroBinding' es el nombre del binding generado para content_registro.xml
import com.example.srl.databinding.ContentRegistroBinding

class msreg : AppCompatActivity() {

    private lateinit var binding: ContentRegistroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Inflar el layout 'content_registro.xml'
        binding = ContentRegistroBinding.inflate(layoutInflater)

        // 2. Establecer la vista raíz (muestra el layout)
        setContentView(binding.root)

        supportActionBar?.hide()
    }
}