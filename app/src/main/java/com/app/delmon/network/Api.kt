package com.app.delmon.network

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.NetworkError
import com.android.volley.NoConnectionError
import com.android.volley.ParseError
import com.android.volley.Request
import com.android.volley.ServerError
import com.android.volley.TimeoutError
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.app.delmon.app.AppController
import com.app.delmon.interfaces.ApiResponseCallback
import com.app.delmon.utils.NetworkUtils.isNetworkConnected
import com.app.delmon.utils.UiUtils
import com.app.delmon.R
import com.app.delmon.Session.SharedHelper


object Api {

    var MY_SOCKET_TIMEOUT_MS = 50000
    var TAG = Api::class.java.simpleName

    private fun noInternetMessage(context: Context?): String {
        return context?.getString(R.string.no_internet_connection)
            ?: "No internet connection"
    }

    private fun mapVolleyErrorToMessage(error: VolleyError, context: Context?): String {
        return when (error) {
            is TimeoutError, is NoConnectionError ->
                context?.getString(R.string.no_internet_connection)
                    ?: "No internet connection"
            is ServerError ->
                context?.getString(R.string.server_error) ?: "Server error"
            is NetworkError ->
                context?.getString(R.string.network_error) ?: "Network error"
            is ParseError ->
                context?.getString(R.string.parsing_error) ?: "Parsing error"
            else ->
                context?.getString(R.string.network_error) ?: "Network error"
        }
    }

    private fun logNetworkDebug(error: VolleyError) {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            val nr = error.networkResponse
            Log.d(TAG, "VolleyError: ${error.javaClass.simpleName} message=${error.message} networkResponse=${nr?.statusCode}")
        }
    }

    private fun handleVolleyError(
        error: VolleyError,
        context: Context?,
        apiResponseCallback: ApiResponseCallback
    ) {
        logNetworkDebug(error)
        when (error) {
            is AuthFailureError -> {
                moveToLoginActivity(context)
                val msg = context?.getString(R.string.session_expired)
                    ?: "Session expired"
                apiResponseCallback.setErrorResponse(msg)
            }
            is TimeoutError, is NoConnectionError -> {
                apiResponseCallback.setErrorResponse(noInternetMessage(context))
            }
            is ServerError, is NetworkError, is ParseError -> {
                apiResponseCallback.setErrorResponse(mapVolleyErrorToMessage(error, context))
            }
            else -> {
                apiResponseCallback.setErrorResponse(mapVolleyErrorToMessage(error, context))
            }
        }
    }

    private fun moveToLoginActivity(context: Context?) {
        val ctx = context ?: return
        SharedHelper(ctx).apply {
            loggedIn = false
            userImage = ""
            token = ""
            id = 0
            name = ""
            email = ""
            mobileNumber = ""
            countryCode = ""
        }
        (ctx as? Activity)?.finish()
    }

    private fun enqueueJsonRequest(
        method: Int,
        input: ApiInput,
        apiResponseCallback: ApiResponseCallback
    ) {
        val jsonObjectRequest = object : JsonObjectRequest(
            method,
            input.url,
            input.jsonObject,
            { response ->
                apiResponseCallback.setResponseSuccess(response)
                UiUtils.showLog("$TAG response", "${input.url} $response")
            },
            { error -> handleVolleyError(error, input.context, apiResponseCallback) }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                return if (input.headers != null) {
                    HashMap<String, String>().apply {
                        for ((key, value) in input.headers!!) {
                            put(key, value)
                        }
                    }
                } else {
                    super.getHeaders()
                }
            }
        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            MY_SOCKET_TIMEOUT_MS,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        UiUtils.showLog("$TAG Request", input.toString())
        AppController.getInstance().addrequestToQueue(jsonObjectRequest)
    }

    fun postMethod(input: ApiInput, apiResponseCallback: ApiResponseCallback) {
        UiUtils.showLog(
            "$TAG Request",
            "${input.url} ${input.headers} ${input.jsonObject}"
        )
        if (isNetworkConnected(input.context)) {
            enqueueJsonRequest(Request.Method.POST, input, apiResponseCallback)
        } else {
            apiResponseCallback.setErrorResponse(noInternetMessage(input.context))
        }
    }

    fun putMethod(input: ApiInput, apiResponseCallback: ApiResponseCallback) {
        UiUtils.showLog(
            "$TAG Request",
            "${input.url} ${input.headers} ${input.jsonObject}"
        )
        if (isNetworkConnected(input.context)) {
            enqueueJsonRequest(Request.Method.PUT, input, apiResponseCallback)
        } else {
            apiResponseCallback.setErrorResponse(noInternetMessage(input.context))
        }
    }

    fun getMethod(input: ApiInput, apiResponseCallback: ApiResponseCallback) {
        UiUtils.showLog("$TAG Request", "${input.url} ${input.headers}")
        if (isNetworkConnected(input.context)) {
            enqueueJsonRequest(Request.Method.GET, input, apiResponseCallback)
        } else {
            apiResponseCallback.setErrorResponse(noInternetMessage(input.context))
        }
    }
}
