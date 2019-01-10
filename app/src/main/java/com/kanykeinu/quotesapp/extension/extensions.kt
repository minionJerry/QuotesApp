package com.kanykeinu.quotesapp.extension

import android.content.Context
import android.widget.Toast

fun Context.showToast(text : CharSequence, duration: Int = Toast.LENGTH_LONG){
    Toast.makeText(this,text,duration).show()
}

fun String.removeCategoryPrefix(){
    if (this.contains(" ПРО "))
        this.replaceBefore("ПРО","")
    else
        this.replaceBefore(" О ","")
}