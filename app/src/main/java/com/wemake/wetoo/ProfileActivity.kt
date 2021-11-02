package com.wemake.wetoo

<<<<<<< HEAD
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

=======
import android.content.ContentValues.TAG
import android.os.Bundle
import android.widget.*
import android.view.*
import android.util.*
import androidx.appcompat.app.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

import com.wemake.wetoo.data.UserProfile
import com.wemake.wetoo.func.Auth
import com.wemake.wetoo.func.Firebase
import java.util.*

class ProfileActivity : AppCompatActivity() {

    var fbAuth : FirebaseAuth? = null
    var fbFirestore : FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        initLayout()

        var university = findViewById<TextView>(R.id.university)
        var name = findViewById<EditText>(R.id.Name)
        var grade = findViewById<EditText>(R.id.Grade)
        var interest = findViewById<TextView>(R.id.interest)

        interest.setOnClickListener {
            var FieldArray = arrayOf("안드로이드", "게임", "보안", "웹", "서버", "AI", "임베디드", "소프트웨어", "DB", "디자인")
            var dlg = AlertDialog.Builder(this)
            dlg.setTitle("관심분야")
            dlg.setItems(FieldArray) { _, which ->
                interest.text = "관심분야 : " + FieldArray[which]
            }
            dlg.show()
        }

        var contect = findViewById<TextView>(R.id.contect)
        var KakaoTalkId : String? = null; var KakaoTalkOpenId : String? = null;
        var Tel : String? = null; var Email : String? = null

        contect.setOnClickListener {
            var dialogView = View.inflate(this, R.layout.contact, null)
            var dlg = AlertDialog.Builder(this)

            var dlgKakaoTalkId = dialogView.findViewById<EditText>(R.id.KakaoTalkId)
            var dlgKakaoTalkOpenId = dialogView.findViewById<EditText>(R.id.KakaoTalkOpenId)
            var dlgTel = dialogView.findViewById<EditText>(R.id.Tel)
            var dlgEmail = dialogView.findViewById<EditText>(R.id.Email)

            dlg.setTitle("연락 수단")
            dlg.setView(dialogView)

            if(!KakaoTalkId.isNullOrEmpty()) dlgKakaoTalkId.setText(KakaoTalkId)
            if(!KakaoTalkOpenId.isNullOrEmpty()) dlgKakaoTalkOpenId.setText(KakaoTalkOpenId)
            if(!Tel.isNullOrEmpty()) dlgTel.setText(Tel)
            if(!Email.isNullOrEmpty()) dlgEmail.setText(Email)

            dlg.setNegativeButton("확인") { _, which ->
                KakaoTalkId = dlgKakaoTalkId.text.toString()
                KakaoTalkOpenId= dlgKakaoTalkOpenId.text.toString()
                Tel = dlgTel.text.toString()
                Email = dlgEmail.text.toString()
            }
            dlg.setPositiveButton("취소", null)
            dlg.show()
        }

        var save = findViewById<Button>(R.id.btnsave)
        save.setOnClickListener {
            fbAuth = FirebaseAuth.getInstance()
            fbFirestore = FirebaseFirestore.getInstance()

            if(true) {
                var userInfo = UserProfile()

                userInfo.name = name.text.toString()
                userInfo.grade = grade.text.toString()
                userInfo.email = Email.toString()
                userInfo.interest = interest.text.toString()
                fbFirestore?.collection("users")?.document(fbAuth?.uid.toString())?.set(userInfo)
            }
            finish()
        }

        var cancel = findViewById<Button>(R.id.btncnl)
>>>>>>> origin/kdm
        cancel.setOnClickListener {
            finish()
        }
    }
<<<<<<< HEAD
=======

    private fun initLayout() {
        fbAuth = FirebaseAuth.getInstance()
        fbFirestore = FirebaseFirestore.getInstance()
    }
>>>>>>> origin/kdm
}