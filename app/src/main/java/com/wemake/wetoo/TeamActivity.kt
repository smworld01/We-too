package com.wemake.wetoo

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.wemake.wetoo.func.Auth
import com.wemake.wetoo.func.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TeamActivity : AppCompatActivity() {
    lateinit var team1 : TextView
    lateinit var team2 : TextView
    lateinit var team3 : TextView
    lateinit var team4 : TextView
    lateinit var team1kakao : TextView
    lateinit var team2kakao : TextView
    lateinit var team3kakao : TextView
    lateinit var team4kakao : TextView
    lateinit var db : Firebase
    lateinit var btnBack : ImageButton

    var uid : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team)

        val user = Auth(this)
        uid = user.getUid()
        db = Firebase(this, uid)
        btnBack = findViewById(R.id.back)

        val scope = CoroutineScope(Job() + Dispatchers.Main)

        team1 = findViewById(R.id.team1)
        team2 = findViewById(R.id.team2)
        team3 = findViewById(R.id.team3)
        team4 = findViewById(R.id.team4)
        team1kakao = findViewById(R.id.team1kakao)
        team2kakao = findViewById(R.id.team2kakao)
        team3kakao = findViewById(R.id.team3kakao)
        team4kakao = findViewById(R.id.team4kakao)

        scope.launch{
            // 모든 사람의 프로필 가져오기
            val profiles = db.getTeamUser()

//            Log.e("test", profiles.toString())
            team1.text = profiles?.map { it?.name }?.get(0)!!
            team2.text = profiles?.map { it?.name }?.get(1)!!
            team3.text = profiles?.map { it?.name }?.get(2)!!
            team4.text = profiles?.map { it?.name }?.get(3)!!

            team1kakao.text =  profiles?.map { it?.ktoid }?.get(0)!!
            team2kakao.text =  profiles?.map { it?.ktoid }?.get(1)!!
            team3kakao.text =  profiles?.map { it?.ktoid }?.get(2)!!
            team4kakao.text =  profiles?.map { it?.ktoid }?.get(3)!!

//
//            // 0번째 사람의 이름 가져오기
//            Log.e("test", profiles?.map { it?.name }?.get(0)!!)
//            // 모든 사람의 카카오톡 오픈 아이디 가져오기
//            Log.e("test", profiles?.map { it?.ktoid }.toString())
        }
        btnBack.setOnClickListener {
            finish()
        }
    }


}