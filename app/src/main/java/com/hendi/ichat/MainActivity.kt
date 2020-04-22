package com.hendi.ichat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.messaging.FirebaseMessaging
import com.hendi.ichat.Fragment.F_Dashboard
import com.hendi.ichat.Help.sharedPrefManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var sp : sharedPrefManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sp = sharedPrefManager(this)
        sp.saveSPBoolean(sharedPrefManager.SP_SUDAH_LOGIN,true)
        supportFragmentManager.beginTransaction().replace(R.id.id_fl_content,F_Dashboard()).commit()
    }
}

