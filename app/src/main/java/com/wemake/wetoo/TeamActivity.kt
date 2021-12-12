package com.wemake.wetoo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import com.wemake.wetoo.func.Firebase

class TeamActivity : AppCompatActivity() {
    lateinit var team1 : EditText
    lateinit var team2 : EditText
    lateinit var team3 : EditText
    lateinit var team4 : EditText
    lateinit var team1kakao : EditText
    lateinit var team2kakao : EditText
    lateinit var team3kakao : EditText
    lateinit var team4kakao : EditText
    lateinit var db : Firebase
    lateinit var btnBack : ImageButton

    var uid : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team)

        btnBack = findViewById(R.id.back)
//        team1 = findViewById(R.id.team1)
//        team2 = findViewById(R.id.team2)
//        team3 = findViewById(R.id.team3)
//        team4 = findViewById(R.id.team4)
//        team1kakao = findViewById(R.id.team1kakao)
//        team2kakao = findViewById(R.id.team2kakao)
//        team3kakao = findViewById(R.id.team3kakao)
//        team4kakao = findViewById(R.id.team4kakao)

//        db.getTeamUser()
        btnBack.setOnClickListener {
            finish()
        }
    }


}