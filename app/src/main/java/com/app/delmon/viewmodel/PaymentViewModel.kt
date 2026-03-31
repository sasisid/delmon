package com.app.delmon.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.delmon.Model.PaymentSession
import com.app.delmon.Session.SharedHelper
import com.app.delmon.interfaces.ApiResponseCallback
import com.app.delmon.network.Api
import com.app.delmon.network.ApiInput
import com.app.delmon.network.UrlHelper
import com.app.delmon.utils.Constants
import com.google.gson.Gson
import org.json.JSONObject
import kotlin.collections.HashMap

class PaymentViewModel(application: Application) : AndroidViewModel(application)  {
    var applicationIns: Application = application
    var sharedHelper: SharedHelper? = SharedHelper(applicationIns.applicationContext)
    private fun getApiParams(
        context: Context,
        jsonObject: JSONObject?,
        methodName: String,
    ): ApiInput {

        val header: MutableMap<String, String> = HashMap()
        header[Constants.ApiKeys.LANG] = sharedHelper!!.language
        header["Authorization"] = "Basic bWVyY2hhbnQuRTE1MjUxOTUwOjg5ZWU5ZWU0MDUzMmY3Zjc5MGJhMmU1YzI4NmZjNWZk"
//        if (sharedHelper!!.id != null ){
//            header["userId"] = sharedHelper!!.id.toString()
//        }
//        header["type"] = sharedHelper!!.appType.toString()
        val apiInputs = ApiInput()
        apiInputs.context = context
        apiInputs.jsonObject = jsonObject
        apiInputs.headers = header
        apiInputs.url = methodName

        return apiInputs
    }

    fun getsessionData(
        context: Context,
    ): LiveData<PaymentSession> {

        val commonResponseModel: MutableLiveData<PaymentSession> = MutableLiveData()
        Api.postMethod(getApiParams(
            context,
            null,
            UrlHelper.PaymentSession
        ), object : ApiResponseCallback {
            override fun setResponseSuccess(jsonObject: JSONObject) {
                Log.d("TAG", "setResponseSuccess: $jsonObject")
                val gson = Gson()
                val response: PaymentSession =  gson.fromJson(jsonObject.toString(), PaymentSession::class.java)
                commonResponseModel.value = response
            }

            override fun setErrorResponse(error: String) {
                val response = PaymentSession()
                response.result = "FAILURE"
                commonResponseModel.value = response
            }

        })

        return commonResponseModel
    }

    fun updatesessionData(
        context: Context,
        sessionId:String,
        jsonObject: JSONObject
    ): LiveData<PaymentSession> {

        val commonResponseModel: MutableLiveData<PaymentSession> = MutableLiveData()
        Api.putMethod(getApiParams(
            context,
            jsonObject,
            UrlHelper.PaymentSession+"/"+sessionId
        ), object : ApiResponseCallback {
            override fun setResponseSuccess(jsonObject: JSONObject) {
                Log.d("TAG", "setResponseSuccess: $jsonObject")
                val gson = Gson()
                val response: PaymentSession =  gson.fromJson(jsonObject.toString(), PaymentSession::class.java)
                commonResponseModel.value = response
            }

            override fun setErrorResponse(error: String) {
                val response = PaymentSession()
                response.result = "FAILURE"
                commonResponseModel.value = response
            }

        })

        return commonResponseModel
    }

}