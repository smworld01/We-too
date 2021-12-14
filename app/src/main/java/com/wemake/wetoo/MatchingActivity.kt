package com.wemake.wetoo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.wemake.wetoo.data.UserProfile
import com.wemake.wetoo.func.Auth
import com.wemake.wetoo.func.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.w3c.dom.Text

class MatchingActivity : AppCompatActivity() {
    lateinit var btnAgree : Button
    lateinit var btnDisagree : Button
    lateinit var btnBack : ImageButton
    lateinit var interest : TextView
    lateinit var tm1 : TextView; lateinit var tm2 : TextView
    lateinit var tm3 : TextView; lateinit var tm4 : TextView

    private var fbAuth: FirebaseAuth? = null
    private var fbFirestore: FirebaseFirestore? = null
    private var fbStorage: FirebaseStorage? = null

    lateinit var db : Firebase
    var uid : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matching)

        btnAgree = findViewById(R.id.button4)
        btnDisagree = findViewById(R.id.button2)
        btnBack = findViewById(R.id.back)
        interest = findViewById(R.id.interest)
        tm1 = findViewById(R.id.tm1); tm2 = findViewById(R.id.tm2)
        tm3 = findViewById(R.id.tm3); tm4 = findViewById(R.id.tm4)

        val user = Auth(this)
        uid = user.getUid()
        db = Firebase(this, uid)

        fbAuth = FirebaseAuth.getInstance()
        fbFirestore = FirebaseFirestore.getInstance()
        fbStorage = FirebaseStorage.getInstance()

        val dbs = fbFirestore?.collection("users")?.document(fbAuth?.uid.toString())
        dbs?.get()?.addOnSuccessListener { documentSnapshot ->
            Log.e("test", "{$documentSnapshot}")
            val users = documentSnapshot.toObject<UserProfile>()
            interest.setText(users?.interest).toString()

            val scope = CoroutineScope(Job() + Dispatchers.Main)

            scope.launch{
                // 모든 사람의 프로필 가져오기
                val profiles = db.getTeamUser()

                tm1.text = profiles?.map { it?.name }?.get(0)!!
                tm1.setOnClickListener {
                    showIntroduction( profiles?.map {it?.introduction}?.get(0)!! )
                }
                tm2.text = profiles?.map { it?.name }?.get(1)!!
                tm2.setOnClickListener {
                    showIntroduction( profiles?.map {it?.introduction}?.get(1)!! )
                }
                tm3.text = profiles?.map { it?.name }?.get(2)!!
                tm3.setOnClickListener {
                    showIntroduction( profiles?.map {it?.introduction}?.get(2)!! )
                }

                tm4.text = profiles?.map { it?.name }?.get(3)!!
                tm4.setOnClickListener {
                    showIntroduction( profiles?.map {it?.introduction}?.get(3)!! )
                }

                /*Log.e("test", profiles.toString())

                // 0번째 사람의 이름 가져오기
                Log.e("test", profiles?.map { it?.name }?.get(0)!!)
                // 모든 사람의 카카오톡 오픈 아이디 가져오기
                Log.e("test", profiles?.map { it?.ktoid }.toString())*/
            }

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
    }

    private fun showIntroduction(profiles : String) {
        var dialogView = View.inflate(this, R.layout.alert_popup, null)
        var dlg = AlertDialog.Builder(this)

        var tv = dialogView.findViewById<TextView>(R.id.Tv)

        dlg.setTitle("자기소개")
        dlg.setView(dialogView)
        tv.text = profiles
        dlg.setNegativeButton("확인",null)
        dlg.show()
    }
}