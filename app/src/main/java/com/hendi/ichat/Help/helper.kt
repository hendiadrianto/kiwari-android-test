@file:Suppress("DEPRECATION")

package com.hendi.kandangku.Helper

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.text.TextUtils
import android.widget.Toast
import com.hendi.ichat.R
import java.text.SimpleDateFormat
import java.util.*


fun isEqual(text1 : String , text2 : String) : Boolean{
    if (text1.equals(text2)) return true else return false
}

fun isEmpty(text: String) : Boolean {
    if (TextUtils.isEmpty(text.trim())) return true else return false
}

fun mToast(mContext : Context,string: String,duration : Int = Toast.LENGTH_SHORT) = Toast.makeText(mContext,string,duration).show()

fun mDate() : String {
    val date = Date()
    val dateFormat = SimpleDateFormat("dd-MM-yyyy hh:mm:ss")
    return dateFormat.format(date)
}

fun mProgress(dialog: Dialog) {
    dialog.setContentView(R.layout.progress_bar)
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setCancelable(false)
    dialog.show()
}

fun mNetworkAvailable(mContext: Context) : Boolean {
    val connectivityManager = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activateNetwokInfo  = connectivityManager.activeNetworkInfo
    return activateNetwokInfo != null && activateNetwokInfo.isConnected
}
