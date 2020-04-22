package com.hendi.ichat

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.hendi.ichat.Fragment.F_Dashboard
import com.hendi.ichat.Help.sharedPrefManager
import com.hendi.kandangku.Helper.isEmpty
import com.hendi.kandangku.Helper.isEqual
import com.hendi.kandangku.Helper.mToast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    lateinit var mContext : Context
    lateinit var sp : sharedPrefManager
    var double = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mContext = this
        sp = sharedPrefManager(mContext)
        //sp.saveSPBoolean(sharedPrefManager.SP_SUDAH_LOGIN,false)

        if (sp.spSudahLogin!!){
            startActivity(Intent(this,MainActivity::class.java))
        }

        id_btn_login.setOnClickListener {
            login()
        }
    }

    private fun login() {
        textInputLayout.isErrorEnabled = false
        textInputLayout2.isErrorEnabled = false

        if (isEqual("jarjit@mail.com",id_email_login.text.toString()) && isEqual("123456",id_password_login.text.toString()) ){
            sp.saveSPString(sharedPrefManager.SP_CHAT_FROM,"Jarjit Singh")
            sp.saveSPString(sharedPrefManager.SP_IMAGE_FROM,"https://api.adorable.io/avatars/160/jarjit@mail.com.png")
            sp.saveSPString(sharedPrefManager.SP_IMAGE_TO,"https://api.adorable.io/avatars/160/ismail@mail.com.png")
            startActivity(Intent(this,MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))

        } else if (isEqual("ismail@mail.com",id_email_login.text.toString()) && isEqual("123456",id_password_login.text.toString()) ){
            sp.saveSPString(sharedPrefManager.SP_CHAT_FROM,"Ismail bin Mail")
            sp.saveSPString(sharedPrefManager.SP_IMAGE_FROM,"https://api.adorable.io/avatars/160/ismail@mail.com.png")
            sp.saveSPString(sharedPrefManager.SP_IMAGE_TO,"https://api.adorable.io/avatars/160/jarjit@mail.com.png")
            startActivity(Intent(this,MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))

        } else if (isEmpty(id_email_login.text.toString())){
            textInputLayout.error = "E-mail masih kosong !"
        } else if (isEmpty(id_password_login.text.toString())){
            textInputLayout2.error = "Password masih kosong !"
        }  else {
            mToast(mContext,"E-mail / Password yang anda masukan salah !")
        }
    }

    override fun onBackPressed() {
        if (double){
            super.onBackPressed()
            return
        }

        this.double = true
        mToast(this,"Tekan tombol kembali lagi untuk keluar")
        Handler().postDelayed(object : Runnable{
            override fun run() {
                double = false
            }
        },2000)
    }
}
