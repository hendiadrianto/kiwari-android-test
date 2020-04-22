package com.hendi.ichat.Help

import android.content.Context
import android.content.SharedPreferences

class sharedPrefManager(context: Context) {

    internal var sp: SharedPreferences
    internal var spEditor: SharedPreferences.Editor

    val spSudahLogin: Boolean?
        get() = sp.getBoolean(SP_SUDAH_LOGIN, false)


    init {
        sp = context.getSharedPreferences(SP_APP, Context.MODE_PRIVATE)
        spEditor = sp.edit()
    }

    fun saveSPString(keySP: String, value: String) {
        spEditor.putString(keySP, value)
        spEditor.commit()
    }

    fun saveSPInt(keySP: String, value: Int) {
        spEditor.putInt(keySP, value)
        spEditor.commit()
    }

    fun saveSPBoolean(keySP: String, value: Boolean) {
        spEditor.putBoolean(keySP, value)
        spEditor.commit()
    }

    companion object {
        val SP_APP = "iChat"

        val SP_NAMA = "username"

        val SP_SUDAH_LOGIN = "spSudahLogin"
    }


}