package com.app.delmon.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.delmon.Model.*
import com.app.delmon.interfaces.ApiResponseCallback
import com.app.delmon.network.Api
import com.app.delmon.network.ApiInput
import com.app.delmon.network.UrlHelper
import com.app.delmon.utils.Constants
import com.google.gson.Gson
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

class AddressViewModel: ViewModel(){


    private fun getApiParams(
        context: Context,
        jsonObject: JSONObject?,
        methodName: String
    ): ApiInput {

        val header: MutableMap<String, String> = HashMap()
        header[Constants.ApiKeys.LANG] = Constants.User.language
        header["Authorization"] = "Bearer "+Constants.User.token
//        if (Constants.User.id != 0 ){
//            header["userId"] = Constants.User.id.toString()
//        }
        if (Constants.User.apptype == 1 ){
            header["type"] = "POULTRY"
        }else{
            header["type"] = "FEEDING"
        }
        val apiInputs = ApiInput()
        apiInputs.context = context
        apiInputs.jsonObject = jsonObject
        apiInputs.headers = header
        apiInputs.url = methodName

        return apiInputs
    }

    fun getPincodes(
        context: Context,
    ): LiveData<PinCodeResponse> {

        val commonResponseModel: MutableLiveData<PinCodeResponse> = MutableLiveData()
        Api.getMethod(getApiParams(
            context,
            null,
            UrlHelper.PIN
        ), object : ApiResponseCallback {
            override fun setResponseSuccess(jsonObject: JSONObject) {
                Log.d("TAG", "setResponseSuccess: $jsonObject")
                val gson = Gson()
                val response: PinCodeResponse =  gson.fromJson(jsonObject.toString(), PinCodeResponse::class.java)
                commonResponseModel.value = response
            }

            override fun setErrorResponse(error: String) {
                val response = PinCodeResponse()
                response.error = true
                commonResponseModel.value = response
            }

        })

        return commonResponseModel
    }

    fun getZone(
        context: Context,
    ): LiveData<ZoneResponse> {
        val commonResponseModel: MutableLiveData<ZoneResponse> = MutableLiveData()
        Api.getMethod(getApiParams(
            context,
            null,
            UrlHelper.ZONE
        ), object : ApiResponseCallback {
            override fun setResponseSuccess(jsonObject: JSONObject) {
                Log.d("TAG", "setResponseSuccess: $jsonObject")
                val gson = Gson()
                val response: ZoneResponse =  gson.fromJson(jsonObject.toString(), ZoneResponse::class.java)
                commonResponseModel.value = response
            }

            override fun setErrorResponse(error: String) {
                Log.e("appSample", "ZONEERROR: $error")
                val response = ZoneResponse()
                response.error = true
                commonResponseModel.value = response
            }

        })

        return commonResponseModel
    }

    fun getFindArea(context: Context, zoneId:String): LiveData<AreaResponse> {
        Log.e("appSample", "AREA_URL: " + UrlHelper.AREA+zoneId)
        val commonResponseModel: MutableLiveData<AreaResponse> = MutableLiveData()
        Api.getMethod(getApiParams(context, null, UrlHelper.AREA+zoneId), object : ApiResponseCallback {
            override fun setResponseSuccess(jsonObject: JSONObject) {
                Log.d("TAG", "setResponseSuccess: $jsonObject")
                val response: AreaResponse =  Gson().fromJson(jsonObject.toString(), AreaResponse::class.java)
                commonResponseModel.value = response
            }
            override fun setErrorResponse(error: String) {
                val response = AreaResponse()
                response.error = true
                commonResponseModel.value = response
            }

        })
        return commonResponseModel
    }

    fun getAddress(
        context: Context,
    ): LiveData<AddressResponse> {

        val commonResponseModel: MutableLiveData<AddressResponse> = MutableLiveData()
        Api.getMethod(getApiParams(
            context,
            null,
            UrlHelper.GETADDRESS
        ), object : ApiResponseCallback {
            override fun setResponseSuccess(jsonObject: JSONObject) {
                Log.d("TAG", "setResponseSuccess: $jsonObject")
                val gson = Gson()
                val response: AddressResponse =  gson.fromJson(jsonObject.toString(), AddressResponse::class.java)
                commonResponseModel.value = response
            }

            override fun setErrorResponse(error: String) {
                val response = AddressResponse()
                response.error = true
                commonResponseModel.value = response
            }

        })

        return commonResponseModel
    }



    fun addAddress(context: Context,jsonObject: JSONObject): LiveData<CommonResponse> {

        val commonResponseModel: MutableLiveData<CommonResponse> = MutableLiveData()

        Api.postMethod(getApiParams(
            context,
            jsonObject,
            UrlHelper.ADDADDRESS
        ), object : ApiResponseCallback {
            override fun setResponseSuccess(jsonObject: JSONObject) {
                Log.d("TAG", "setResponseSuccess: $jsonObject")
                val gson = Gson()
                val response: CommonResponse =
                    gson.fromJson(jsonObject.toString(), CommonResponse::class.java)
                commonResponseModel.value = response
            }

            override fun setErrorResponse(error: String) {
                val response = CommonResponse()
                response.success = true
                commonResponseModel.value = response
            }

        })

        return commonResponseModel
    }
    fun deleteAddress(context: Context,id:Int): LiveData<CommonResponse> {

        val commonResponseModel: MutableLiveData<CommonResponse> = MutableLiveData()

        Api.postMethod(getApiParams(
            context,
            null,
            UrlHelper.DELETEADDRESS+"/$id"
        ), object : ApiResponseCallback {
            override fun setResponseSuccess(jsonObject: JSONObject) {
                Log.d("TAG", "setResponseSuccess: $jsonObject")
                val gson = Gson()
                val response: CommonResponse =
                    gson.fromJson(jsonObject.toString(), CommonResponse::class.java)
                commonResponseModel.value = response
            }

            override fun setErrorResponse(error: String) {
                val response = CommonResponse()
                response.success = true
                commonResponseModel.value = response
            }

        })

        return commonResponseModel
    }
    fun editAddress(context: Context,id:Int,jsonObject: JSONObject): LiveData<CommonResponse> {

        val commonResponseModel: MutableLiveData<CommonResponse> = MutableLiveData()

        Api.postMethod(getApiParams(
            context,
            jsonObject,
            UrlHelper.ADDADDRESS+"/$id"
        ), object : ApiResponseCallback {
            override fun setResponseSuccess(jsonObject: JSONObject) {
                Log.d("TAG", "setResponseSuccess: $jsonObject")
                val gson = Gson()
                val response: CommonResponse =
                    gson.fromJson(jsonObject.toString(), CommonResponse::class.java)
                commonResponseModel.value = response
            }

            override fun setErrorResponse(error: String) {
                val response = CommonResponse()
                response.success = true
                commonResponseModel.value = response
            }

        })

        return commonResponseModel
    }


}