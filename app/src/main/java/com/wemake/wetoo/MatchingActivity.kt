package com.wemake.wetoo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
<<<<<<< HEAD
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.wemake.wetoo.data.UserProfile
=======
import android.widget.Button
import android.widget.ImageButton
>>>>>>> origin/gsh6451341
import com.wemake.wetoo.func.Auth
import com.wemake.wetoo.func.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
<<<<<<< HEAD
import org.w3c.dom.Text
=======
>>>>>>> origin/gsh6451341

class MatchingActivity : AppCompatActivity() {
    lateinit var btnAgree : Button
    lateinit var btnDisagree : Button
    lateinit var btnBack : ImageButton
<<<<<<< HEAD
    lateinit var interest : TextView
    lateinit var tm1 : TextView; lateinit var tm2 : TextView
    lateinit var tm3 : TextView; lateinit var tm4 : TextView

    private var fbAuth: FirebaseAuth? = null
    private var fbFirestore: FirebaseFirestore? = null
    private var fbStorage: FirebaseStorage? = null

=======
>>>>>>> origin/gsh6451341
    lateinit var db : Firebase
    var uid : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matching)

        btnAgree = findViewById(R.id.button4)
        btnDisagree = findViewById(R.id.button2)
        btnBack = findViewById(R.id.back)
<<<<<<< HEAD
        interest = findViewById(R.id.interest)
        tm1 = findViewById(R.id.tm1); tm2 = findViewById(R.id.tm2)
        tm3 = findViewById(R.id.tm3); tm4 = findViewById(R.id.tm4)
=======
>>>>>>> origin/gsh6451341

        val user = Auth(this)
        uid = user.getUid()
        db = Firebase(this, uid)

<<<<<<< HEAD
        fbAuth = FirebaseAuth.getInstance()
        fbFirestore = FirebaseFirestore.getInstance()
        fbStorage = FirebaseStorage.getInstance()

        val dbs = fbFirestore?.collection("users")?.document(fbAuth?.uid.toString())
        dbs?.get()?.addOnSuccessListener { documentSnapshot ->
            Log.e("test", "{$documentSnapshot}")
            val users = documentSnapshot.toObject<UserProfile>()
            interest.setText(users?.interest).toString()

            val scope = CoroutineScope(Job() + Dispatchers.Main)

            btnAgree.setOnClickListener {
                scope.launch {
                    db.matchAgree()
                }
            }

            btnDisagree.setOnClickListener {
                scope.launch {
                    db.matchDisagree()
                }
            }

            btnBack.setOnClickListener {
                finish()
            }
        }
=======

        val scope = CoroutineScope(Job() + Dispatchers.Main)

        btnAgree.setOnClickListener {
            scope.launch {
                db.matchAgree()
            }
        }

        btnDisagree.setOnClickListener {
            scope.launch {
                db.matchDisagree()
            }
        }

        btnBack.setOnClickListener {
            finish()
        }


>>>>>>> origin/gsh6451341
    }
}