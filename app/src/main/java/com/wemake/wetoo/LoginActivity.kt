package com.wemake.wetoo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.SignInButton
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.iid.FirebaseInstanceIdReceiver
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal
import com.google.firebase.installations.FirebaseInstallations
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
            }
        }
    }
}