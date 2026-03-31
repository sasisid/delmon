package com.app.delmon.network

import android.app.Activity
import android.content.Context
import android.os.StrictMode
import android.util.Log
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.app.delmon.app.AppController
import com.app.delmon.interfaces.ApiResponseCallback
import com.app.delmon.utils.NetworkUtils.isNetworkConnected
import com.app.delmon.utils.UiUtils
import com.app.delmon.R
import com.app.delmon.Session.SharedHelper
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


object Api {

    var MY_SOCKET_TIMEOUT_MS = 50000
    var TAG = Api::class.java.simpleName
//    private var requestQueue: RequestQueue? = null
//    private fun getRequestQueue(): RequestQueue? {
//
//        if (requestQueue == null)
//           requestQueue = Volley.newRequestQueue(instance)
//
//        return requestQueue
//    }
//
//    fun <T> addrequestToQueue(request: Request<T>) {
//        request.tag = AppController.TAG
//        getRequestQueue()?.add(request)
//    }

    fun postMethod(input: ApiInput, apiResponseCallback: ApiResponseCallback) {
        UiUtils.showLog("$TAG Requestsss", "${input.url.toString()} ${input.headers.toString()} ${input.jsonObject.toString()}")
        if (isNetworkConnected(input.context)) {
            Log.d("cmnb","hgv")
            var jsonObjectRequest =
                object : JsonObjectRequest(Method.POST, input.url, input.jsonObject, {
                    Log.d("cmnb",""+it)
                    apiResponseCallback.setResponseSuccess(it)
                    Log.d("cmnb",""+it)
                    UiUtils.showLog("$TAG response", "${input.url.toString()} $it")
                }, {
                    Log.d("cmnb",""+it)
                    Log.d("cmnb",""+it.localizedMessage)
                    Log.d("cmnb1",""+it.networkResponse.toString())
                    when (it) {
                        is TimeoutError, is NoConnectionError -> {
                            input.context?.getString(R.string.no_internet_connection)
                                ?.let { it1 -> apiResponseCallback.setErrorResponse(it1) }
                        }
                        is AuthFailureError -> {
                            moveToLoginActivity(input.context)
                //                    input.context?.getString(R.string.session_expired)?.let { it1 -> apiResponseCallback.setErrorResponse(it1) }
                        }
                        is ServerError -> {
                            input.context?.getString(R.string.server_error)
                                ?.let { it1 -> apiResponseCallback.setErrorResponse(it1) }
                        }
                        is NetworkError -> {
                            input.context?.getString(R.string.network_error)
                                ?.let { it1 -> apiResponseCallback.setErrorResponse(it1) }
                        }
                        is ParseError -> {
                            input.context?.getString(R.string.parsing_error)
                                ?.let { it1 -> apiResponseCallback.setErrorResponse(it1) }
                        }
                        else -> {
                            input.context?.getString(R.string.network_error)
                                ?.let { it1 -> apiResponseCallback.setErrorResponse(it1) }
                        }
                    }
                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        return if (input.headers != null) {
                            val params: HashMap<String, String> = HashMap<String, String>()

                            for ((key, value) in input.headers!!) {
                                params[key] = value
                            }
                            params
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
        } else {
            apiResponseCallback.setErrorResponse("No Internet Connection")
        }
    }

    fun putMethod(input: ApiInput, apiResponseCallback: ApiResponseCallback) {
        UiUtils.showLog("$TAG Requestsss", "${input.url.toString()} ${input.headers.toString()} ${input.jsonObject.toString()}")
        if (isNetworkConnected(input.context)) {
            Log.d("cmnb","hgv")
            var jsonObjectRequest =
                object : JsonObjectRequest(Method.PUT, input.url, input.jsonObject, {
                    Log.d("cmnb",""+it)
                    apiResponseCallback.setResponseSuccess(it)
                    Log.d("cmnb",""+it)
                    UiUtils.showLog("$TAG response", "${input.url.toString()} $it")
                }, {
                    Log.d("cmnb",""+it)
                    Log.d("cmnb",""+it.localizedMessage)
                    Log.d("cmnb1",""+it.networkResponse.toString())
                    when (it) {
                        is TimeoutError, is NoConnectionError -> {
                            input.context?.getString(R.string.no_internet_connection)
                                ?.let { it1 -> apiResponseCallback.setErrorResponse(it1) }
                        }
                        is AuthFailureError -> {
                            moveToLoginActivity(input.context)
                //                    input.context?.getString(R.string.session_expired)?.let { it1 -> apiResponseCallback.setErrorResponse(it1) }
                        }
                        is ServerError -> {
                            input.context?.getString(R.string.server_error)
                                ?.let { it1 -> apiResponseCallback.setErrorResponse(it1) }
                        }
                        is NetworkError -> {
                            input.context?.getString(R.string.network_error)
                                ?.let { it1 -> apiResponseCallback.setErrorResponse(it1) }
                        }
                        is ParseError -> {
                            input.context?.getString(R.string.parsing_error)
                                ?.let { it1 -> apiResponseCallback.setErrorResponse(it1) }
                        }
                        else -> {
                            input.context?.getString(R.string.network_error)
                                ?.let { it1 -> apiResponseCallback.setErrorResponse(it1) }
                        }
                    }
                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        return if (input.headers != null) {
                            val params: HashMap<String, String> = HashMap<String, String>()

                            for ((key, value) in input.headers!!) {
                                params[key] = value
                            }
                            params
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
        } else {
            apiResponseCallback.setErrorResponse("No Internet Connection")
        }
    }


    private fun moveToLoginActivity(context: Context?) {


        context?.let {

            SharedHelper(it).loggedIn = false
            SharedHelper(it).userImage = ""
            SharedHelper(it).token = ""
            SharedHelper(it).id = 0
            SharedHelper(it).name = ""
            SharedHelper(it).email = ""
            SharedHelper(it).mobileNumber = ""
            SharedHelper(it).countryCode = ""
//            val intent = Intent(it, LoginActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            it.startActivity(intent)
            (it as Activity).finish()
        }

    }

    fun getMethod(input: ApiInput, apiResponseCallback: ApiResponseCallback) {

        UiUtils.showLog("$TAG Request", "${input.url.toString()} ${input.headers.toString()}")

        if (isNetworkConnected(input.context)) {
            val jsonObjectRequest =
                object : JsonObjectRequest(
                    Method.GET, input.url, input.jsonObject, {
                    apiResponseCallback.setResponseSuccess(it)
                    Log.d(TAG, "getMethodstart: $it")
                    UiUtils.showLog("$TAG Response", "${input.url.toString()} $it")
                },
                    {

                    Log.d(TAG, "getMethod: $it")

                        when (it) {
                            is TimeoutError, is NoConnectionError -> {
                                input.context?.getString(R.string.no_internet_connection)
                                    ?.let { it1 -> apiResponseCallback.setErrorResponse(it1) }
                            }
                            is AuthFailureError -> {
                                moveToLoginActivity(input.context)
                    //                        input.context?.getString(R.string.session_expired)
                    //                            ?.let { it1 -> apiResponseCallback.setErrorResponse(it1) }
                            }
                            is ServerError -> {
                                input.context?.getString(R.string.server_error)
                                    ?.let { it1 -> apiResponseCallback.setErrorResponse(it1) }
                            }
                            is NetworkError -> {
                                input.context?.getString(R.string.network_error)
                                    ?.let { it1 -> apiResponseCallback.setErrorResponse(it1) }
                            }
                            is ParseError -> {
                                input.context?.getString(R.string.parsing_error)
                                    ?.let { it1 -> apiResponseCallback.setErrorResponse(it1) }
                            }
                            else -> {
                                input.context?.getString(R.string.network_error)
                                    ?.let { it1 -> apiResponseCallback.setErrorResponse(it1) }
                            }
                        }
                })
                    {
                    override fun getHeaders(): MutableMap<String, String> {
                        return if (input.headers != null) {
                            val params: HashMap<String, String> = HashMap<String, String>()

                            for ((key, value) in input.headers!!) {
                                params[key] = value
                            }
                            params
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
        } else {
            apiResponseCallback.setErrorResponse("No Internet Connection")
        }
    }

//    fun uploadImage(
//        file: File,
//        url: String,
//        context: Context,
//        apiResponseCallback: ApiResponseCallback
//    ) {
//        if (isNetworkConnected(context)) {
//
//            val MEDIA_TYPE_PNG = MediaType.parse("image/jpeg")
//
//            val requestBody = MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart(
//                    "file",
//                    "fileName",
//                    RequestBody.create(MEDIA_TYPE_PNG, File(file.path))
//                )
//                .build()
//            val request = okhttp3.Request.Builder()
//                .url(url)
//                .post(requestBody).build()
//            Log.e(TAG, "file: " + file.path)
//
//            Coroutien.iOWorker {
//                try {
//                    var response = uploadRequest(request)
//                    val jsonObject = JSONObject(response.body()!!.string())
//                    apiResponseCallback.setResponseSuccess(jsonObject)
//                } catch (e: IOException) {
//                    Log.e(TAG, "IOException: $e")
//                    apiResponseCallback.setErrorResponse(e.message.toString())
//                } catch (e: JSONException) {
//                    apiResponseCallback.setErrorResponse(e.message.toString())
//                }
//            }
//
//        } else {
//            apiResponseCallback.setErrorResponse("No Internet Connection")
//        }
//    }

    private fun uploadRequest(request: okhttp3.Request): okhttp3.Response {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(30, TimeUnit.SECONDS)
        builder.readTimeout(30, TimeUnit.SECONDS)
        builder.writeTimeout(30, TimeUnit.SECONDS)
        val client = builder.build()
        val response = client.newCall(request).execute()
        return response
    }


}