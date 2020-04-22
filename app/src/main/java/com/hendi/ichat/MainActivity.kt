package com.hendi.ichat

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessaging
import com.hendi.ichat.Fragment.F_Dashboard
import com.hendi.ichat.Help.sharedPrefManager
import com.hendi.kandangku.Helper.mToast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var sp : sharedPrefManager
    var double = false

    lateinit var mContext : Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mContext = this
        sp = sharedPrefManager(this)
        sp.saveSPBoolean(sharedPrefManager.SP_SUDAH_LOGIN,true)
        supportFragmentManager.beginTransaction().replace(R.id.id_fl_content,F_Dashboard()).commit()
    }

    override fun onBackPressed() {

        if (!sp.spBack!!){
            if (double){
                //super.onBackPressed()
                moveTaskToBack(true)
                //return
            }

            this.double = true
            mToast(mContext,"Tekan tombol kembali lagi untuk keluar")

            Handler().postDelayed(object : Runnable{
                override fun run() {
                    double = false
                }
            },2000)
        } else {
            sp.saveSPBoolean(sharedPrefManager.SP_BACK,false)
            supportFragmentManager.beginTransaction().replace(R.id.id_fl_content,F_Dashboard()).commit()
        }
    }
}

