package com.wemake.wetoo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.R
import com.wemake.wetoo.func.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MatchingActivity : AppCompatActivity() {
    lateinit var btnAgree : Button
    lateinit var btnDisagree : Button
    lateinit var db : Firebase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matching)

        btnAgree = findViewById(R.id.button4)
        btnDisagree = findViewById(R.id.button2)

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
    }
}