package com.app.delmon.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.delmon.Model.CheckUserResponse
import com.app.delmon.Model.SettingsResponses
import com.app.delmon.Model.UserResponse
import com.app.delmon.Session.SharedHelper
import com.app.delmon.interfaces.ApiResponseCallback
import com.app.delmon.network.Api
import com.app.delmon.network.ApiInput
import com.app.delmon.network.UrlHelper
import com.app.delmon.utils.Constants
import com.app.delmon.utils.postFromJson
import org.json.JSONObject

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    var applicationIns: Application = application
    var sharedHelper: SharedHelper? = SharedHelper(applicationIns.applicationContext)
    private fun getApiParams(
        context: Context,
        jsonObject: JSONObject?,
        methodName: String
    ): ApiInput {

        val header: MutableMap<String, String> = HashMap()
        header[Constants.ApiKeys.LANG] = sharedHelper!!.language
        header["Authorization"] = "Bearer "+Constants.User.token
        if (sharedHelper!!.id != null ){
            header["userId"] = sharedHelper!!.id.toString()
        }
        header["type"] = sharedHelper!!.appType.toString()
        val apiInputs = ApiInput()
        apiInputs.context = context
        apiInputs.jsonObject = jsonObject
        apiInputs.headers = header
        apiInputs.url = methodName

        return apiInputs
    }

    fun checkUser(
        context: Context,
        mNum: String,
        cCode: String,
    ): LiveData<CheckUserResponse> {
        val jsonObject = JSONObject()
        jsonObject.put("mobileNumber", mNum)
        jsonObject.put("countryCode", cCode)
        val commonResponseModel: MutableLiveData<CheckUserResponse> = MutableLiveData()
        Api.postMethod(getApiParams(
            context,
            jsonObject,
            UrlHelper.CHECKUSER
        ), object : ApiResponseCallback {
            override fun setResponseSuccess(jsonObject: JSONObject) {
                Log.d("TAG", "setResponseSuccess: $jsonObject")
                commonResponseModel.postFromJson(jsonObject, CheckUserResponse::class.java) { err ->
                    CheckUserResponse().apply { error = true; message = err }
                }
            }

            override fun setErrorResponse(error: String) {
                commonResponseModel.value = CheckUserResponse().apply { this.error = true; message = error }
            }

        })

        return commonResponseModel
    }

    fun verifyOtp(
        context: Context,
        mNum: String,
        cCode: String,
        otp: String,
    ): LiveData<UserResponse> {
        val jsonObject = JSONObject()
        jsonObject.put("mobileNumber", mNum)
        jsonObject.put("otp", otp)
        jsonObject.put("countryCode", cCode)
        val commonResponseModel: MutableLiveData<UserResponse> = MutableLiveData()
        Api.postMethod(getApiParams(
            context,
            jsonObject,
            UrlHelper.VERIFYOTP
        ), object : ApiResponseCallback {
            override fun setResponseSuccess(jsonObject: JSONObject) {
                Log.d("TAG", "setResponseSuccess: $jsonObject")
                commonResponseModel.postFromJson(jsonObject, UserResponse::class.java) { err ->
                    UserResponse().apply { error = true; message = err }
                }
            }

            override fun setErrorResponse(error: String) {
                commonResponseModel.value = UserResponse().apply { this.error = true; message = error }
            }

        })

        return commonResponseModel
    }

    fun createUser(
        context: Context,
        mNum: String,
        uType: String,
        cCode: String,
        crNumber: String,
    ): LiveData<CheckUserResponse> {
        val jsonObject = JSONObject()
        jsonObject.put("mobileNumber", mNum)
        jsonObject.put("userType", uType)
        jsonObject.put("countryCode", cCode)
        jsonObject.put("crNumber", crNumber)
        val commonResponseModel: MutableLiveData<CheckUserResponse> = MutableLiveData()
        Api.postMethod(getApiParams(
            context,
            jsonObject,
            UrlHelper.CREATEUSER
        ), object : ApiResponseCallback {
            override fun setResponseSuccess(jsonObject: JSONObject) {
                Log.d("TAG", "setResponseSuccess: $jsonObject")
                commonResponseModel.postFromJson(jsonObject, CheckUserResponse::class.java) { err ->
                    CheckUserResponse().apply { error = true; message = err }
                }
            }

            override fun setErrorResponse(error: String) {
                commonResponseModel.value = CheckUserResponse().apply { this.error = true; message = error }
            }

        })

        return commonResponseModel
    }

    fun updateUser(
        context: Context,
        uname: String,
        email: String,
        dToken: String,
    ): LiveData<UserResponse> {
        val jsonObject = JSONObject()
        jsonObject.put("userName", uname)
        jsonObject.put("email", email)
        jsonObject.put("deviceToken", sharedHelper?.fcmToken)
        val commonResponseModel: MutableLiveData<UserResponse> = MutableLiveData()
        Api.postMethod(getApiParams(
            context,
            jsonObject,
            UrlHelper.UPDATEUSER
        ), object : ApiResponseCallback {
            override fun setResponseSuccess(jsonObject: JSONObject) {
                Log.d("TAG", "setResponseSuccess: $jsonObject")
                commonResponseModel.postFromJson(jsonObject, UserResponse::class.java) { err ->
                    UserResponse().apply { error = true; message = err }
                }
            }

            override fun setErrorResponse(error: String) {
                commonResponseModel.value = UserResponse().apply { this.error = true; message = error }
            }

        })

        return commonResponseModel
    }

    fun getUser(
        context: Context,
    ): LiveData<UserResponse> {

        val commonResponseModel: MutableLiveData<UserResponse> = MutableLiveData()
        Api.getMethod(getApiParams(
            context,
            null,
            UrlHelper.USER
        ), object : ApiResponseCallback {
            override fun setResponseSuccess(jsonObject: JSONObject) {
                Log.d("TAG", "setResponseSuccess: $jsonObject")
                commonResponseModel.postFromJson(jsonObject, UserResponse::class.java) { err ->
                    UserResponse().apply { error = true; message = err }
                }
            }

            override fun setErrorResponse(error: String) {
                commonResponseModel.value = UserResponse().apply { this.error = true; message = error }
            }

        })

        return commonResponseModel
    }


    fun getSettings(
        context: Context,
    ): LiveData<SettingsResponses> {

        val commonResponseModel: MutableLiveData<SettingsResponses> = MutableLiveData()
        Api.getMethod(getApiParams(
            context,
            null,
            UrlHelper.URL
        ), object : ApiResponseCallback {
            override fun setResponseSuccess(jsonObject: JSONObject) {
                Log.d("TAG", "setResponseSuccess: $jsonObject")
                commonResponseModel.postFromJson(jsonObject, SettingsResponses::class.java) {
                    SettingsResponses(success = false)
                }
            }

            override fun setErrorResponse(error: String) {
                commonResponseModel.value = SettingsResponses(success = false)
            }

        })

        return commonResponseModel
    }

}