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
                       val db = db.collection("matching").document()
                       db.set(mt)
                       it.reference.update("matchRef", db.path)
                   } else {
//                       val mt = v.documents[0].toObject<MatchTable>()!!
                       v.documents.forEach { table ->
                           val mt = table.toObject<MatchTable>()!!
                           if(mt.users!!.size < 4){
                               mt.users?.add(it.reference)
                               mt.approvals?.add("waiting")
                               table.reference.update("users", mt.users)
                               table.reference.update("approvals", mt.approvals)
                               it.reference.update("matchRef", table.reference)
                               return@forEach
                           }
                       }
                       val mt = MatchTable(data as String?, mutableListOf(it.reference), mutableListOf("waiting"))
                       val db = db.collection("matching").document()
                       db.set(mt)
                       it.reference.update("matchRef", db.path)
                   }
               }

           }

       }
    }

    fun matchAgree(){
        db.collection("matching").document(uid!!).get().addOnSuccessListener{
            val query = db.collection("matching").whereEqualTo("users", uid).get()
            query.addOnSuccessListener { value ->
//                val mt = value.documents[].toObject<MatchTable>()
//                mt?.approvals?.add("agree")
//                value.documents[].reference.update("approvals", mt?.approvals)
            }
//            인덱스의 값을 가져와서 수정하면 될듯
        }
    }

    fun matchDisagree(){
        db.collection("matching").document(uid!!).get().addOnSuccessListener{
            val query = db.collection("matching").whereEqualTo("users", uid).get()
            query.addOnSuccessListener { value ->
//                val mt = value.documents[].toObject<MatchTable>()
//                mt?.approvals?.add("disagree")
//                value.documents[].reference.update("approvals", mt?.approvals)
            }
//            인덱스의 값을 가져와서 수정하면 될듯
        }
    }

    fun teamBuild(){
//        여기에서 approvals의 값이 모두 수락이면 이제 team 문서로 옮기고 matching에 있는 것 삭제
    }

    suspend fun isMatching():Boolean{
        return getUserProfile()?.await()?.get("matchRef")!=null
    }


}