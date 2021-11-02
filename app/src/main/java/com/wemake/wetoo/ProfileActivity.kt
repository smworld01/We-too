package com.wemake.wetoo

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.toObject
import com.wemake.wetoo.data.UserProfile
import com.wemake.wetoo.func.Auth
import com.wemake.wetoo.func.Firebase

class ProfileActivity : AppCompatActivity() {


    lateinit var save : Button
    lateinit var cancel : Button
    lateinit var university :

    override fun onCreate(savedInstanceState: Bundle?) {
        val user = Auth(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        save = findViewById<Button>(R.id.btnsave)
        cancel = findViewById<Button>(R.id.btncnl)


        save.setOnClickListener {
            val uid = user.getUid()
            val db = Firebase(this, uid)
            val userProfile = UserProfile(
                "강릉원주대학교",
                4,
                "smworld02@gmail.com",
                listOf("게임", "안드로이드"),
                "https://123.com",
                "안녕하세요.",
                "조인철"
            )
            db.setUserProfile(userProfile)
            Toast.makeText(applicationContext, "저장되었습니다.", Toast.LENGTH_SHORT)
        }

        cancel.setOnClickListener {
            finish()
        }
    }
}