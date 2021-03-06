package com.wemake.wetoo.func

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
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

    suspend fun matching() {
//        if (uid === null) return null
        Log.e("test", "test")
       db.collection("users").document(uid!!).get().addOnSuccessListener {
           val data = it.data?.get("interest")
           Log.e("asd", "$data")
           val query = db.collection("matching").whereEqualTo("interest", data).get()
           query.addOnSuccessListener { value ->
               Log.e("asd", "${value?.documents?.size}")
               value?.let { v ->
                   if (v.documents.size == 0) {
                       val mt = MatchTable(data as String?, mutableListOf(it.reference), mutableListOf("waiting"), "matching")
                       val collection = db.collection("matching")
                       collection.add(mt).addOnSuccessListener { documentReference ->
                           it.reference.update("matchRef", documentReference)
                       }
                   } else {
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
                   }
               }

           }

       }.addOnFailureListener {
           Log.e("test", it.toString())
           it.printStackTrace()
       }
    }

    suspend fun matchAgree(){
        getUserProfile()?.await()?.apply {
            val profileReference = reference
            val profile = toObject<UserProfile>()!!
            profile.matchRef?.get()?.await()?.apply {
                val mt = toObject<MatchTable>()!!
                mt.users?.indexOf(profileReference)?.let {
                    mt.approvals?.set(it, "agree")
                }
                reference.update("approvals", mt.approvals)

            }
        }
    }

    suspend fun matchDisagree(){
        getUserProfile()?.await()?.apply {
            val profileReference = reference
            val profile = toObject<UserProfile>()!!
            profile.matchRef?.get()?.await()?.apply {
                val mt = toObject<MatchTable>()!!
                mt.users?.indexOf(profileReference)?.let {
                    mt.approvals?.set(it, "disagree")
                }
                reference.update("approvals", mt.approvals)
            }
        }
    }

    suspend fun isMatching():Boolean{
        return getUserProfile()?.await()?.get("matchRef")!=null
    }

    suspend fun isMatched():Boolean{
        val myProfile = getUserProfile()?.await()
        val profile = myProfile?.toObject<UserProfile>()!!
        profile.matchRef?.get()?.await()?.apply {
            val mt = toObject<MatchTable>()!!
            return mt.state == "matched"
        }
        return false
    }

    suspend fun isTemporaryMatched():Boolean {
        val myProfile = getUserProfile()?.await()
        val profile = myProfile?.toObject<UserProfile>()!!
        profile.matchRef?.get()?.await()?.apply {
            val mt = toObject<MatchTable>()!!
            return mt.users!!.size == 4
        }
        return false
    }

        suspend fun getTeamUser(): List<UserProfile?>? {
            val myProfile = getUserProfile()?.await()
            val profile = myProfile?.toObject<UserProfile>()!!

            profile.matchRef?.get()?.await()?.apply {
                val mt = toObject<MatchTable>()!!
                return mt.users?.map {
                    it.get().await().toObject<UserProfile>()
                }
            }
            return null
        }

        suspend fun userNumber(execute: (Int) -> Unit) {
            db.collection("users")
                .get().await().apply {
                    execute(this.documents.size)
                }
        }

        suspend fun teams(execute: (Int) -> Unit) {
            db.collection("matching").whereEqualTo("state", "matched")
                .get().await().apply {
                    execute(this.documents.size)
                }
        }
    }


