package com.hendi.ichat.Help

import android.content.Context
import android.content.SharedPreferences

class sharedPrefManager(context: Context) {

    internal var sp: SharedPreferences
    internal var spEditor: SharedPreferences.Editor

    val spSudahLogin: Boolean?
        get() = sp.getBoolean(SP_SUDAH_LOGIN, false)

    val spChatFrom: String?
        get() = sp.getString(SP_CHAT_FROM, "")

    val spChatTo: String?
        get() = sp.getString(SP_CHAT_TO, "")

    val spImageFrom : String?
        get() = sp.getString(SP_IMAGE_FROM,"")

    val spImageTo : String?
        get() = sp.getString(SP_IMAGE_TO,"")

    val spBack : Boolean?
        get() = sp.getBoolean(SP_BACK,false)


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

        val SP_IMAGE_FROM = "spImageFrom"
        val SP_IMAGE_TO = "spImageTo"

        val SP_SUDAH_LOGIN = "spSudahLogin"

        val SP_CHAT_FROM = "sp_chat_from"
        val SP_CHAT_TO = "sp_chat_to"

        val SP_BACK = "spBack"
    }


}