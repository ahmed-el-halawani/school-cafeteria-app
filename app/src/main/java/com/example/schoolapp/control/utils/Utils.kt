package com.example.schoolapp.control.utils

import android.annotation.SuppressLint
import android.content.Context
import java.text.SimpleDateFormat
import java.util.Calendar
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import com.example.schoolapp.R

@SuppressLint("SimpleDateFormat")
object Utils {
    fun cd():String{
        val date = Calendar.getInstance().time
        val sdf = SimpleDateFormat("dd-MM-YY hh:mm")
        return sdf.format(date)
    }

    fun isNotConnect(context:Context):Boolean{
        val info = (context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetwork
        return info == null
    }

    fun randomColor():Int{
        val colorList = arrayListOf(
            R.color.c1,
            R.color.c2,
            R.color.c3,
            R.color.c4,
            R.color.c5,
            R.color.c6
        )
        return colorList.random()
    }
}