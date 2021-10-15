package com.wemake.wetoo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wemake.wetoo.func.Auth
import android.widget.*

class MainActivity : AppCompatActivity() {

    lateinit var btnpro : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnpro = findViewById<Button>(R.id.button)

        val user = Auth(this)

        if (user.isNotSignIn()) {
            val intent = Intent(this, LoginActivity::class.java)

            startActivity(intent)
        }

        btnpro.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
}