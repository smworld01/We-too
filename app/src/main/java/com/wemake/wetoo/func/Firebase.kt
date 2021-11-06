package com.wemake.wetoo.func

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.wemake.wetoo.data.MatchTable
import com.wemake.wetoo.data.UserProfile
import kotlinx.coroutines.tasks.await

class Firebase(private val activity: AppCompatActivity, private val uid: String?) {
    private val db = Firebase.firestore

    fun getUserProfile(): Task<DocumentSnapshot>? {
        if (uid === null) return null

        return db.collection("users").document(uid)
            .get()
    }

    fun setUserProfile(userProfile: UserProfile): Task<Void>? {
        if (uid === null) return null

        return db.collection("users").document(uid)
            .set(userProfile)
    }

    fun updateUserProfile(data: Map<String, Any>): Task<Void>? {
        if (uid === null) return null

        return db.collection("users").document(uid)
            .update(data)
    }

    fun changeVisibility(visibility: Boolean): Task<Void>? {
        if (uid === null) return null

        return updateUserProfile(mapOf(
            "visibility" to if(visibility) "Public" else "Private"
        ))
    }

    fun matching() {
//        if (uid === null) return null

       db.collection("users").document(uid!!).get().addOnSuccessListener {
           val data = it.data?.get("interest")
           Log.e("asd", "$data")
           val query = db.collection("matching").whereEqualTo("interest", data).get()
           query.addOnSuccessListener { value ->
               Log.e("asd", "${value?.documents?.size}")
               value?.let { v ->
                   if (v.documents.size == 0) {
                       val mt = MatchTable(data as String?, mutableListOf(it.reference), mutableListOf("waiting"))
                       db.collection("matching").add(mt)
                   } else {
                       val mt = v.documents[0].toObject<MatchTable>()
                       mt?.users?.add(it.reference)
                       mt?.approvals?.add("waiting")
                       v.documents[0].reference.update("users", mt?.users)
                       v.documents[0].reference.update("approvals", mt?.approvals)
                       it.reference.update("matchRef", v.documents[0].reference)
                   }
               }

           }

       }
    }

    suspend fun isMatching():Boolean{
        return getUserProfile()?.await()?.get("matchRef")!=null
    }
}