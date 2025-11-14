package com.example.srl

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class DeleteConfirmationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_confirmation)

        val yesButton = findViewById<Button>(R.id.button_yes)
        val noButton = findViewById<Button>(R.id.button_no)

        yesButton.setOnClickListener {
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        noButton.setOnClickListener {
            finish()
        }
    }
}