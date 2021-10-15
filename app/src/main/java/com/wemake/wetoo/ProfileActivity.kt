package com.wemake.wetoo

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class profile_update : AppCompatActivity() {

    lateinit var save : Button
    lateinit var cancel : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        cancel = findViewById<Button>(R.id.btncnl)

        cancel.setOnClickListener {
            finish()
        }
    }
}