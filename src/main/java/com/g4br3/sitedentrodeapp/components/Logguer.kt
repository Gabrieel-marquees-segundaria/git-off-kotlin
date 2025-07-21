package com.g4br3.sitedentrodeapp.components
import android.util.Log


class Logguer(
    tag: String,
    callbackLog: (message: String) -> Unit

) {

    var TAG = tag

    public fun info(TAG: String=this.TAG,     message:String){
        Log.d(TAG,message)

    }}