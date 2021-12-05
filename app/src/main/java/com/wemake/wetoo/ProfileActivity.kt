package com.wemake.wetoo

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.*
import android.view.*
import android.widget.*
import androidx.appcompat.app.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.*
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.wemake.wetoo.data.UserProfile
import java.util.*

class ProfileActivity : AppCompatActivity() {

    private var fbAuth: FirebaseAuth? = null
    private var fbFirestore: FirebaseFirestore? = null
    private var fbStorage: FirebaseStorage? = null
    lateinit var image : ImageView
    var uriPhoto : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        fbAuth = FirebaseAuth.getInstance()
        fbFirestore = FirebaseFirestore.getInstance()
        fbStorage = FirebaseStorage.getInstance()

        val db = fbFirestore?.collection("users")?.document(fbAuth?.uid.toString())

        db?.get()?.addOnSuccessListener { documentSnapshot ->
            Log.e("test","{$documentSnapshot}")
            val user = documentSnapshot.toObject<UserProfile>()
            Log.e("test","2")
            var name = findViewById<EditText>(R.id.Name)
            var grade = findViewById<EditText>(R.id.Grade)
            var university = findViewById<TextView>(R.id.university)
            var interest = findViewById<TextView>(R.id.interest)
            var introduction = findViewById<EditText>(R.id.introduction)
            image = findViewById(R.id.imageView)

            name.setText(user?.name).toString()
            grade.setText(user?.grade).toString()
            university.text = user?.university
            interest.text = user?.interest
            introduction.setText(user?.introduction)

            Glide.with(this)
                .load(user?.imageUrl)
                .into(image)

            var url = user?.imageUrl

            image.setOnClickListener {
                ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
                selectGallery()
            }

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

            var ktId = user?.ktid
            var ktoId = user?.ktoid
            var tel = user?.tel
            var email = user?.email

            contact.setOnClickListener {
                var dialogView = View.inflate(this, R.layout.contact, null)
                var dlg = AlertDialog.Builder(this)

                var dlgKakaoTalkId = dialogView.findViewById<EditText>(R.id.KakaoTalkId)
                var dlgKakaoTalkOpenId = dialogView.findViewById<EditText>(R.id.KakaoTalkOpenId)
                var dlgTel = dialogView.findViewById<EditText>(R.id.Tel)
                var dlgEmail = dialogView.findViewById<EditText>(R.id.Email)

                dlg.setTitle("연락 수단")
                dlg.setView(dialogView)

                if (!ktId.isNullOrEmpty()) dlgKakaoTalkId.setText(ktId)
                if (!ktoId.isNullOrEmpty()) dlgKakaoTalkOpenId.setText(ktoId)
                if (!tel.isNullOrEmpty()) dlgTel.setText(tel)
                if (!email.isNullOrEmpty()) dlgEmail.setText(email)

                dlg.setPositiveButton("저장") { _, which ->
                    if(dlgKakaoTalkId.getText().toString() == "") {
                        checkEvent()
                    }
                    ktId = dlgKakaoTalkId.text.toString()
                    ktoId = dlgKakaoTalkOpenId.text.toString()
                    tel = dlgTel.text.toString()
                    email = dlgEmail.text.toString()
                }
                dlg.setNegativeButton("취소",null)
                dlg.show()
            }


            var save = findViewById<Button>(R.id.btnsave)
            save.setOnClickListener {
                val uid = Firebase.auth.currentUser!!.uid

                FirebaseMessaging.getInstance().token.addOnSuccessListener {

                    var userInfo = UserProfile(
                        name = name.text.toString(),
                        grade = grade.text.toString(),
                        university = " 강릉원주대학교 ",
                        ktid = ktId,
                        ktoid = ktoId,
                        tel = tel,
                        email = email,
                        interest = interest.text.toString(),
                        introduction = introduction.text.toString(),
                        imageUrl = url,
                        fcm_token = it
                    )
                    Log.e("test", it)

                    Firebase.firestore.collection("users").document(uid).set(userInfo)
                    imageUp()
                }

                finish()
                }
            }

        var cancel = findViewById<Button>(R.id.btncnl)
        cancel.setOnClickListener {
            finish()
        }
    }

    private fun selectGallery() {
        var intent = Intent(Intent.ACTION_PICK)
        intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        intent.type = "image/*"
        startActivityForResult(intent,1)
    }

    private fun checkEvent() {
        val view = View.inflate(this,R.layout.alert_popup,null)
        val textView: TextView = view.findViewById(R.id.Tv)
        textView.text = "필수 입력 항목을 확인해주세요"

        val alertDialog = AlertDialog.Builder(this)
            .setPositiveButton("확인") { dialog, which -> }
            .create()

        alertDialog.setView(view)
        alertDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                uriPhoto = data?.data
                image.setImageURI(uriPhoto)

                if (ContextCompat.checkSelfPermission(
                        image!!.context,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                }
            }
        }
    }

    private fun imageUp() {
        val imgFileName = fbAuth?.uid + ".png"
        val storageRef = fbStorage?.reference?.child("profiles/images")?.child(imgFileName)

        uriPhoto?.let {
            storageRef?.putFile(it)?.addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->

                    val userInfo = UserProfile()
                    userInfo.imageUrl = uri.toString()

                    fbFirestore?.collection("users")?.document(fbAuth?.uid.toString())?.update("imageUrl",userInfo.imageUrl.toString())
                }
            }
        }
    }
}