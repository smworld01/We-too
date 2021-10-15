package com.wemake.wetoo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.SignInButton
import com.google.firebase.firestore.ktx.toObject
import com.wemake.wetoo.data.UserProfile
import com.wemake.wetoo.func.Auth
import com.wemake.wetoo.func.Firebase

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initLayout()
    }

    private fun initLayout() {
        val user = Auth(this)
        val googleLoginButton: SignInButton = findViewById(R.id.google_sign_in_button)

        googleLoginButton.setOnClickListener {
            if (user.isNotSignIn()) {
                user.signIn()
            } else {
                val uid = user.getUid()
                val db = Firebase(this, uid)

                val userProfile = UserProfile(
                    "강릉원주대학교",
                    4,
                    "smworld02@gmail.com",
                    listOf("게임", "안드로이드"),
                    "https://123.com",
                    "안녕하세요."
                )
                db.setUserProfile(userProfile)

//                프로필 읽기
//                db.getUserProfile()?.addOnSuccessListener { documentSnapshot ->
//                    val userProfile = documentSnapshot.toObject<UserProfile>()
//                    Log.e("get", "$userProfile")
//                }


            }
        }
    }
}