package com.app.delmon.interfaces

import android.util.Log

interface SmsListener {
    fun messageReceived(messageText: String?){
        Log.d("dvdfv",""+messageText);
    }
}