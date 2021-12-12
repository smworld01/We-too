package com.wemake.wetoo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.wemake.wetoo.func.Auth
import com.wemake.wetoo.func.Firebase
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {

    lateinit var btnpro : Button
    lateinit var btnmat : Button
    lateinit var btnmatch : Button
    lateinit var btnteam : Button
    lateinit var db : Firebase
    var uid : String? = null

    private var matchingAsync : Deferred<Unit>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnpro = findViewById<Button>(R.id.button)
        btnmat = findViewById<Button>(R.id.button3)
        btnmatch = findViewById<Button>(R.id.button5)
        btnteam = findViewById(R.id.button6)

        val user = Auth(this)
        uid = user.getUid()
        db = Firebase(this, uid)

        if (user.isNotSignIn()) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        btnpro.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        val scope = CoroutineScope(Job() + Dispatchers.Main)

        btnmat.setOnClickListener {
            if (matchingAsync == null) {
                matchingAsync = scope.async {
                    Log.e("test", "0")
                    if (db.isMatching()) {
                        Log.e("test", "1")
                        Toast.makeText(this@MainActivity, "이미 매칭중입니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.e("test", "2")
                        db.matching()
                    }
                }
            } else {
                Toast.makeText(this@MainActivity, "이미 매칭중입니다.", Toast.LENGTH_SHORT).show()
            }
        }

        btnmatch.setOnClickListener {

        }

        btnteam.setOnClickListener {
            scope.launch{
                // 모든 사람의 프로필 가져오기
                val profiles = db.getTeamUser()

                Log.e("test", profiles.toString())

                // 0번째 사람의 이름 가져오기
                Log.e("test", profiles?.map { it?.name }?.get(0)!!)
                // 모든 사람의 카카오톡 오픈 아이디 가져오기
                Log.e("test", profiles?.map { it?.ktoid }.toString())
            }
        }

    }
}