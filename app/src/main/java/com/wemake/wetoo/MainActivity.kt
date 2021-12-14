package com.wemake.wetoo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.wemake.wetoo.func.Auth
import com.wemake.wetoo.func.Firebase
import kotlinx.coroutines.*
<<<<<<< HEAD
=======
import org.w3c.dom.Text
>>>>>>> origin/gsh6451341


class MainActivity : AppCompatActivity() {

    lateinit var btnpro : Button
    lateinit var btnmat : Button
    lateinit var btnmatch : Button
    lateinit var btnteam : Button
    lateinit var db : Firebase
<<<<<<< HEAD
=======
    lateinit var tvuser : TextView
    lateinit var tvteams : TextView
>>>>>>> origin/gsh6451341
    var uid : String? = null

    private var matchingAsync : Deferred<Unit>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val scope = CoroutineScope(Job() + Dispatchers.Main)

<<<<<<< HEAD
=======
        tvuser = findViewById(R.id.textView7)
        tvteams = findViewById(R.id.textView5)
>>>>>>> origin/gsh6451341
        btnpro = findViewById<Button>(R.id.button)
        btnmat = findViewById<Button>(R.id.button3)
        btnmatch = findViewById<Button>(R.id.button5)
        btnteam = findViewById(R.id.button6)

        val user = Auth(this)
        uid = user.getUid()
        db = Firebase(this, uid)


<<<<<<< HEAD
        scope.launch {
            db.userNumber()
        }
=======

>>>>>>> origin/gsh6451341
        if (user.isNotSignIn()) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        btnpro.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }



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
<<<<<<< HEAD
            val intent = Intent(this, MatchingActivity::class.java)
            startActivity(intent)
        }

        btnteam.setOnClickListener {
            scope.launch {
                if (db.getTeamUser() == null) {
                    Toast.makeText(this@MainActivity, "아직 매칭이 진행중입니다..", Toast.LENGTH_SHORT).show()
                }else{
                    val intent = Intent(this@MainActivity, TeamActivity::class.java)
                    startActivity(intent)
                }
=======
            scope.launch {
                if (db.isTemporaryMatched()) {
                    val intent = Intent(applicationContext, MatchingActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@MainActivity, "매칭해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }


        btnteam.setOnClickListener {
            scope.launch {
                if (db.isMatched()) {
                    val intent = Intent(this@MainActivity, TeamActivity::class.java)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this@MainActivity, "아직 매칭이 진행중입니다..", Toast.LENGTH_SHORT).show()
                }
            }
        }


        scope.launch {
            db.teams {
                tvteams.text = it.toString()
            }
            db.userNumber {
                tvuser.text = it.toString()
>>>>>>> origin/gsh6451341
            }
        }

    }
}