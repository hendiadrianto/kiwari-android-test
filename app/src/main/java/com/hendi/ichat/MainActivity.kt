package com.hendi.ichat

import android.content.ClipData
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.messaging.FirebaseMessaging
import com.hendi.ichat.Fragment.F_Dashboard
import com.hendi.ichat.Fragment.F_Login
import com.hendi.ichat.Help.sharedPrefManager

class MainActivity : AppCompatActivity() {

    lateinit var sp : sharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        sp = sharedPrefManager(this)

        if (sp.spSudahLogin == false){
            supportFragmentManager.beginTransaction().replace(R.id.id_fl_content,F_Login()).commit()
        } else {
            supportFragmentManager.beginTransaction().replace(R.id.id_fl_content,F_Dashboard()).commit()
        }
    }
}

