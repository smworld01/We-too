package com.wemake.wetoo.func

import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.ktx.Firebase
import com.wemake.wetoo.R


class Auth(private val activity: AppCompatActivity) {
    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(activity.getString(R.string.google_client_id))
        .requestEmail()
        .build()

    private val googleSignInClient: GoogleSignInClient by lazy {
            GoogleSignIn.getClient(activity, gso)
        }

    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    private val getContent = activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult: ActivityResult ->
        val data = activityResult.data

        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            // Google Sign In was successful, authenticate with Firebase
            val account = task.getResult(ApiException::class.java)!!
            Log.d("google_login", "firebaseAuthWithGoogle:" + account.id)
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            // Google Sign In failed, update UI appropriately
            Log.w("google_login", "Google sign in failed", e)
        }
    }

    fun isSignIn(): Boolean {
        val account = GoogleSignIn.getLastSignedInAccount(activity)

        return account != null
    }

    fun isNotSignIn(): Boolean {
        return !isSignIn()
    }

    fun signOut() {
        auth.signOut()
    }

    fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        getContent.launch(signInIntent)
    }

    fun getUid(): String? {
        return auth.currentUser?.uid
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("google_login", "signInWithCredential:success")
                    val user = auth.currentUser
                    val uid = user!!.uid
                    val db = Firebase.firestore

                    FirebaseInstallations.getInstance().getToken(false).addOnSuccessListener {
                        db.collection("users").document(uid)
                            .update(mapOf("fcm_token" to it.token))
                    }

                    activity.finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("google_login", "signInWithCredential:failure", task.exception)
                }
            }
    }
}