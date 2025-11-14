package com.example.srl

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class EditSuccessActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_success)

        val backArrow = findViewById<ImageView>(R.id.back_arrow)
        backArrow.setOnClickListener {
            finish() // Cierra la actividad y vuelve a la anterior
        }
    }
}