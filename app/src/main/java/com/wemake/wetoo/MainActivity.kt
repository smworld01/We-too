package com.wemake.wetoo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.wemake.wetoo.func.Auth
import android.widget.*
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.toObject
import com.wemake.wetoo.data.UserProfile
import com.wemake.wetoo.func.Firebase


class MainActivity : AppCompatActivity() {

    lateinit var btnpro : Button
    lateinit var btnmat : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnpro = findViewById<Button>(R.id.button)
        btnmat = findViewById<Button>(R.id.button3)

        val user = Auth(this)

        if (user.isNotSignIn()) {
            val intent = Intent(this, LoginActivity::class.java)

            startActivity(intent)
        }

        btnpro.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        btnmat.setOnClickListener {
            val uid = user.getUid()
            val db = Firebase(this, uid)

            db.setMatchingTable()
            val userProfile = UserProfile(
//                db.collection("profiles").document(uid)
//                여기에서 matchREF 값을 변경 matching 컬렉션에서 interest가 같은게 있으면 값을 추가 없으면 새로 생성
            )
//            db.setUserProfile(userProfile)
        }



    }
}