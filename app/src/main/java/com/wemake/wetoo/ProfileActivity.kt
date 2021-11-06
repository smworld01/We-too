package com.wemake.wetoo

import android.content.ContentValues.TAG
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
import java.nio.file.attribute.UserPrincipal
import java.util.*

class ProfileActivity : AppCompatActivity() {

    var fbAuth : FirebaseAuth? = null
    var fbFirestore : FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        initLayout()

        fbAuth = FirebaseAuth.getInstance()
        fbFirestore = FirebaseFirestore.getInstance()

        var nameT : String? = null

        val db = fbFirestore?.collection("users")?.document(fbAuth?.uid.toString())
        db?.get()?.addOnSuccessListener { documentSnapshot ->
            val user = documentSnapshot.toObject<UserProfile>()

            var name = findViewById<EditText>(R.id.Name)
            var grade = findViewById<EditText>(R.id.Grade)
            var university = findViewById<TextView>(R.id.university)
            var interest = findViewById<TextView>(R.id.interest)
            var introduction = findViewById<EditText>(R.id.introduction)

            name.setText(user?.name)
            grade.setText(user?.grade)
            university.setText(user?.university)
            interest.setText(user?.interest)
            introduction.setText(user?.introduction)

            interest.setOnClickListener {
                var FieldArray =
                    arrayOf("안드로이드", "게임", "보안", "웹", "서버", "AI", "임베디드", "소프트웨어", "DB", "디자인")
                var dlg = AlertDialog.Builder(this)
                dlg.setTitle("관심분야")
                dlg.setItems(FieldArray) { _, which ->
                    interest.text = FieldArray[which]
                }
                dlg.show()
            }

            var contect = findViewById<TextView>(R.id.contect)
            var KakaoTalkId: String? = ""
            var KakaoTalkOpenId: String? = ""
            var Tel: String? = ""
            var Email: String? = ""

            KakaoTalkId = user?.ktid
            KakaoTalkOpenId = user?.ktoid
            Tel = user?.tel
            Email = user?.email

            contect.setOnClickListener {
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

                dlg.setNegativeButton("확인") { _, which ->
                    KakaoTalkId = dlgKakaoTalkId.text.toString()
                    KakaoTalkOpenId = dlgKakaoTalkOpenId.text.toString()
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

                if (true) {
                    var userInfo = UserProfile()

                    userInfo.name = name.text.toString()
                    userInfo.grade = grade.text.toString()
                    userInfo.university = "강릉원주대학교"
                    userInfo.ktid = KakaoTalkId.toString()
                    userInfo.ktoid = KakaoTalkOpenId.toString()
                    userInfo.tel = Tel.toString()
                    userInfo.email = Email.toString()
                    userInfo.interest = interest.text.toString()
                    userInfo.introduction = introduction.text.toString()
                    fbFirestore?.collection("users")?.document(fbAuth?.uid.toString())
                        ?.set(userInfo)
                }
                finish()
            }

            var cancel = findViewById<Button>(R.id.btncnl)
            cancel.setOnClickListener {
                finish()
            }
        }
    }

    private fun initLayout() {


        /*fbFirestore?.collection("users")?.document(fbAuth?.uid.toString())?.get()?.addOnSuccessListener { result ->
            for(document in result) {
                Log.d(TAG, "${document.id} => ${document.data}")
                Toast.makeText(applicationContext, document.id, Toast.LENGTH_SHORT).show()
            }
        }*///users profile data 가져오기
        /*val db = fbFirestore?.collection("users")?.document(fbAuth?.uid.toString())
        db?.get()?.addOnSuccessListener { document ->
            if(document != null) {
                Log.d(TAG, "DocumentSnapshot data: ${document.data}")
            } else {
                Log.d(TAG, "No such document")
            }
<<<<<<< HEAD
            finish()
        }

        var cancel = findViewById<Button>(R.id.btncnl)

        cancel.setOnClickListener {
            finish()
=======
>>>>>>> 117eb62912cc00f07db2ffe1d7534a619cd8dcd9
        }
            ?.addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }*/
    }

<<<<<<< HEAD

    private fun initLayout() {
        fbAuth = FirebaseAuth.getInstance()
        fbFirestore = FirebaseFirestore.getInstance()
    }

}
=======
}
>>>>>>> 117eb62912cc00f07db2ffe1d7534a619cd8dcd9
