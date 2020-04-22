package com.hendi.kandangku.Helper

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.hendi.ichat.R
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


fun isEqual(text1 : String , text2 : String) : Boolean{
    if (text1.equals(text2)) return true else return false
}

fun isEmpty(text: String) : Boolean {
    if (TextUtils.isEmpty(text.trim())) return true else return false
}


fun mToast(mContext : Context,string: String,duration : Int = Toast.LENGTH_SHORT) = Toast.makeText(mContext,string,duration).show()

fun mProgress(dialog: Dialog) {
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setCancelable(false)
    dialog.show()
}

fun mDate() : String {
    val date = Date()
    val dateFormat = SimpleDateFormat("dd-MM-yyyy")
    return dateFormat.format(date)
}

fun mDate(string : String) : String{
    val now = Calendar.getInstance()

    val date = string.substring(startIndex = 0,endIndex = 10)
    val split = date.split("-")
    val array = ArrayList<String>()
    for (i in split){
        array.add(i)
    }

    var bulan = ""
    when(array[1].toInt()){
        1 -> bulan = "Jan"
        2 -> bulan = "Feb"
        3 -> bulan = "Mar"
        4 -> bulan = "Apr"
        5 -> bulan = "Mei"
        6 -> bulan = "Jun"
        7 -> bulan = "Jul"
        8 -> bulan = "Agt"
        9 -> bulan = "Sep"
        10 -> bulan = "Okt"
        11 -> bulan = "Nov"
        else -> bulan = "Des"
    }

    return array[0] + "-" + bulan + "-" + array[2]
}

fun mDateFormat() : DateFormat{
    return SimpleDateFormat("dd-MM-yyyy")
}

fun mRe(text : String) : String {
    val re = Regex("[^A-Za-z0-9 ]")
    return re.replace(text,"")
}