package com.app.delmon.app

import android.app.Application
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.firebase.analytics.FirebaseAnalytics
import com.mastercard.gateway.android.sdk.GatewayRegion
import com.mastercard.gateway.android.sdk.GatewaySDK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.emvco.threeds.core.ui.UiCustomization

class AppController : Application() {


    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        instance = this
    }

    companion object {

        private val TAG: String = AppController::class.java.simpleName
        private var instance: AppController? = null
        private var requestQueue: RequestQueue? = null
        private var mFirebaseAnalytics: FirebaseAnalytics? = null
        private val callTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toString()

        @Synchronized
        fun getInstance(): AppController {
            return instance as AppController
        }

    }


    private fun getRequestQueue(): RequestQueue? {

        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(instance)

        return requestQueue
    }

    fun <T> addrequestToQueue(request: Request<T>) {
        request.tag = TAG
        getRequestQueue()?.add(request)
    }

    override fun onCreate() {
        super.onCreate()

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        mFirebaseAnalytics?.setAnalyticsCollectionEnabled(true)
        GlobalScope.launch (Dispatchers.Main){
            var a =  GatewaySDK.initialize(this@AppController,"E15251950","DELMON POULTRY COMPANY BSC","https://credimax.gateway.mastercard.com/ma/",GatewayRegion.SAUDI_ARABIA,
                UiCustomization()
            )
            Log.d(TAG, "onCreate:appcontroller "+a)

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            createNotificationChannel()
//            createNotificationChannelCall()
        }
    }

}
