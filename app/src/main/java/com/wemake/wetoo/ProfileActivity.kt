package com.wemake.wetoo

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.widget.*
import android.view.*
import android.util.*
import androidx.appcompat.app.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.*
import com.google.firebase.firestore.ktx.toObject

import com.wemake.wetoo.data.UserProfile
import com.wemake.wetoo.func.Auth
import com.wemake.wetoo.func.Firebase
//import kotlinx.android.synthetic.main.activity_profile.*
import java.nio.file.attribute.UserPrincipal
import java.util.*

class ProfileActivity : AppCompatActivity() {

    private var fbAuth: FirebaseAuth? = null
    private var fbFirestore: FirebaseFirestore? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        fbAuth = FirebaseAuth.getInstance()
        fbFirestore = FirebaseFirestore.getInstance()

        val db = fbFirestore?.collection("users")?.document(fbAuth?.uid.toString())

        db?.get()?.addOnSuccessListener { documentSnapshot ->
            val user = documentSnapshot.toObject<UserProfile>()

            var name = findViewById<EditText>(R.id.Name)
            var grade = findViewById<EditText>(R.id.Grade)
            var university = findViewById<TextView>(R.id.university)
            var interest = findViewById<TextView>(R.id.interest)
            var introduction = findViewById<EditText>(R.id.introduction)

            name.setText(user?.name).toString()
            grade.setText(user?.grade).toString()
            university.text = user?.university
            interest.text = user?.interest
            introduction.setText(user?.introduction)

            interest.setOnClickListener {
                var interestField =
                    arrayOf("안드로이드", "게임", "보안", "웹", "서버", "AI", "임베디드", "소프트웨어", "DB", "디자인")
                var dlg = AlertDialog.Builder(this)
                dlg.setTitle("관심분야")
                dlg.setItems(interestField) { _, which ->
                    interest.text = interestField[which]
                }
                dlg.show()
            }


            var contact = findViewById<TextView>(R.id.contact)

            ktId = user?.ktid
            ktoId = user?.ktoid
            tel = user?.tel
            email = user?.email

            contact.setOnClickListener {
                var dialogView = View.inflate(this, R.layout.contact, null)
                var dlg = AlertDialog.Builder(this)

                var dlgKakaoTalkId = dialogView.findViewById<EditText>(R.id.KakaoTalkId)
                var dlgKakaoTalkOpenId = dialogView.findViewById<EditText>(R.id.KakaoTalkOpenId)
                var dlgTel = dialogView.findViewById<EditText>(R.id.Tel)
                var dlgEmail = dialogView.findViewById<EditText>(R.id.Email)

                dlg.setTitle("연락 수단")
                dlg.setView(dialogView)

                if (!KakaoTalkId.isNullOrEmpty()) dlgKakaoTalkId.setText(KakaoTalkId)
                if (!KakaoTalkOpenId.isNullOrEmpty()) dlgKakaoTalkOpenId.setText(KakaoTalkOpenId)
                if (!Tel.isNullOrEmpty()) dlgTel.setText(Tel)
                if (!Email.isNullOrEmpty()) dlgEmail.setText(Email)

                dlg.setPositiveButton("저장") { _, which ->
                    if(dlgKakaoTalkId.getText().toString() == "") {
                        contactEvent()
                    }
                    KakaoTalkId = dlgKakaoTalkId.text.toString()
                    KakaoTalkOpenId = dlgKakaoTalkOpenId.text.toString()
                    Tel = dlgTel.text.toString()
                    Email = dlgEmail.text.toString()
                    contact.setText(KakaoTalkId)
                }
                dlg.setNegativeButton("취소",null)
                dlg.show()
            }


            var save = findViewById<Button>(R.id.btnsave)
            save.setOnClickListener {
                fbAuth = FirebaseAuth.getInstance()
                fbFirestore = FirebaseFirestore.getInstance()

                var userInfo = UserProfile()
                var check = 0

                userInfo.name = name.text.toString()
                userInfo.grade = grade.text.toString()
                userInfo.university = " 강릉원주대학교 "
                userInfo.ktid = KakaoTalkId.toString()
                userInfo.ktoid = KakaoTalkOpenId.toString()
                userInfo.tel = Tel.toString()
                userInfo.email = Email.toString()
                userInfo.interest = interest.text.toString()
                userInfo.introduction = introduction.text.toString()
                fbFirestore?.collection("users")?.document(fbAuth?.uid.toString())?.set(userInfo)

                finish()
                }
            contact.setText(KakaoTalkId)
            }

        var cancel = findViewById<Button>(R.id.btncnl)
        cancel.setOnClickListener {
            finish()
        }
    }

    private fun contactEvent() {
        val view = View.inflate(this,R.layout.alert_popup,null)
        val textView: TextView = view.findViewById(R.id.Tv)
        textView.text = "필수 입력 항목을 확인해주세요"

        val alertDialog = AlertDialog.Builder(this)
            .setPositiveButton("확인") { dialog, which -> }
            .create()

        alertDialog.setView(view)
        alertDialog.show()
    }
}