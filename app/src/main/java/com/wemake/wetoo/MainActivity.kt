package com.wemake.wetoo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wemake.wetoo.func.Auth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val user = Auth(this)

        if (user.isNotSignIn()) {
            val intent = Intent(this, LoginActivity::class.java)

            startActivity(intent)
        }
    }
}