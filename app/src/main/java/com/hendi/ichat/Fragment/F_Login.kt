package com.hendi.ichat.Fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hendi.ichat.Help.sharedPrefManager
import com.hendi.ichat.R
import com.hendi.kandangku.Helper.isEmpty
import com.hendi.kandangku.Helper.isEqual
import com.hendi.kandangku.Helper.mToast
import kotlinx.android.synthetic.main.f_login.view.*
import kotlinx.android.synthetic.main.f_login.view.id_btn_login as id_btn_login1

class F_Login() : Fragment(){

    lateinit var v : View
    lateinit var mContext : Context
    lateinit var sp : sharedPrefManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = layoutInflater.inflate(R.layout.f_login,container,false)
        mContext = this.context!!
        sp = sharedPrefManager(mContext)

        v.id_btn_login1.setOnClickListener {
            v.textInputLayout.isErrorEnabled = false
            v.textInputLayout2.isErrorEnabled = false

            if (isEqual("jarjit@mail.com",v.id_email_login.text.toString()) && isEqual("123456",v.id_password_login.text.toString()) ){
                sp.saveSPBoolean(sharedPrefManager.SP_SUDAH_LOGIN,true)
                fragmentManager!!.beginTransaction().replace(R.id.id_fl_content,F_Dashboard()).commit()
            } else if (isEqual("ismail@mail.com",v.id_email_login.text.toString()) && isEqual("123456",v.id_password_login.text.toString()) ){
                sp.saveSPBoolean(sharedPrefManager.SP_SUDAH_LOGIN,true)
                fragmentManager!!.beginTransaction().replace(R.id.id_fl_content,F_Dashboard()).commit()
            } else if (isEmpty(v.id_email_login.text.toString())){
                v.textInputLayout.error = "E-mail masih kosong !"
            } else if (isEmpty(v.id_password_login.text.toString())){
                v.textInputLayout2.error = "Password masih kosong !"
            }  else {
                mToast(mContext,"E-mail / Password yang anda masukan salah !")
            }
        }


        return v
    }

}