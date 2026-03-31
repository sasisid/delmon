package com.app.delmon.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager



object NetworkUtils {

    @SuppressLint("MissingPermission")
    fun isNetworkConnected(context: Context?):Boolean{
        val connectivityManager : ConnectivityManager? = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


        var isConnected = false
        if (connectivityManager != null) {

            val activeNetwork = connectivityManager.activeNetworkInfo
            isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting

        }

        return isConnected
    }

}