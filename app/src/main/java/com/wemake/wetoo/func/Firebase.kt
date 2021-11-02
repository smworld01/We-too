package com.wemake.wetoo.func

import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.wemake.wetoo.data.UserProfile

class Firebase(private val activity: AppCompatActivity, private val uid: String?) {
    private val db = Firebase.firestore

    fun getUserProfile(): Task<DocumentSnapshot>? {
        if (uid === null) return null

        return db.collection("profiles").document(uid)
            .get()
    }

    fun setUserProfile(userProfile: UserProfile): Task<Void>? {
        if (uid === null) return null

        return db.collection("profiles").document(uid)
            .set(userProfile)
    }

    fun updateUserProfile(data: Map<String, Any>): Task<Void>? {
        if (uid === null) return null

        return db.collection("profiles").document(uid)
            .update(data)
    }

    fun changeVisibility(visibility: Boolean): Task<Void>? {
        if (uid === null) return null

        return updateUserProfile(mapOf(
            "visibility" to if(visibility) "Public" else "Private"
        ))
    }

//    fun setMatchingTable(){
//        if (uid === null) return null
//
//        db.collection("profiles").document(uid).get().result.data['Inte']
//    }
}