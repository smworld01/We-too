package com.wemake.wetoo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wemake.wetoo.func.Auth
import android.widget.*
<<<<<<< HEAD
import com.wemake.wetoo.data.UserProfile
import com.wemake.wetoo.func.Firebase
=======
>>>>>>> origin/kdm

class MainActivity : AppCompatActivity() {

    lateinit var btnpro : Button
<<<<<<< HEAD

=======

>>>>>>> origin/kdm
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnpro = findViewById<Button>(R.id.button)
<<<<<<< HEAD

=======

>>>>>>> origin/kdm
        val user = Auth(this)

        if (user.isNotSignIn()) {
            val intent = Intent(this, LoginActivity::class.java)

            startActivity(intent)
        }

        btnpro.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
<<<<<<< HEAD

=======
>>>>>>> origin/kdm
    }
}