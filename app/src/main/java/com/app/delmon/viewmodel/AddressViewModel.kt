package com.app.delmon.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.delmon.Model.*
import com.app.delmon.Session.SharedHelper
import com.app.delmon.interfaces.ApiResponseCallback
import com.app.delmon.network.Api
import com.app.delmon.network.ApiInput
import com.app.delmon.network.UrlHelper
import com.app.delmon.utils.Constants
import com.app.delmon.utils.postFromJson
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
        header[Constants.ApiKeys.LANG] = SharedHelper(context).language
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
                commonResponseModel.postFromJson(jsonObject, PinCodeResponse::class.java) {
                    PinCodeResponse(error = true)
                }
            }

            override fun setErrorResponse(error: String) {
                commonResponseModel.value = PinCodeResponse(error = true)
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
                commonResponseModel.postFromJson(jsonObject, ZoneResponse::class.java) {
                    ZoneResponse(error = true)
                }
            }

            override fun setErrorResponse(error: String) {
                commonResponseModel.value = ZoneResponse(error = true)
            }

        })

        return commonResponseModel
    }

    fun getFindArea(context: Context, zoneId:String): LiveData<AreaResponse> {
        Log.d("TAG", "getFindArea zoneId=$zoneId")
        val commonResponseModel: MutableLiveData<AreaResponse> = MutableLiveData()
        Api.getMethod(getApiParams(context, null, UrlHelper.AREA+zoneId), object : ApiResponseCallback {
            override fun setResponseSuccess(jsonObject: JSONObject) {
                Log.d("TAG", "setResponseSuccess: $jsonObject")
                commonResponseModel.postFromJson(jsonObject, AreaResponse::class.java) {
                    AreaResponse(error = true)
                }
            }
            override fun setErrorResponse(error: String) {
                commonResponseModel.value = AreaResponse(error = true)
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
                commonResponseModel.postFromJson(jsonObject, AddressResponse::class.java) { err ->
                    AddressResponse().apply { error = true; message = err }
                }
            }

            override fun setErrorResponse(error: String) {
                commonResponseModel.value = AddressResponse().apply { this.error = true; message = error }
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
                commonResponseModel.postFromJson(jsonObject, CommonResponse::class.java) { err ->
                    CommonResponse().apply { error = true; message = err }
                }
            }

            override fun setErrorResponse(error: String) {
                commonResponseModel.value = CommonResponse().apply { this.error = true; message = error }
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
                commonResponseModel.postFromJson(jsonObject, CommonResponse::class.java) { err ->
                    CommonResponse().apply { error = true; message = err }
                }
            }

            override fun setErrorResponse(error: String) {
                commonResponseModel.value = CommonResponse().apply { this.error = true; message = error }
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
                commonResponseModel.postFromJson(jsonObject, CommonResponse::class.java) { err ->
                    CommonResponse().apply { error = true; message = err }
                }
            }

            override fun setErrorResponse(error: String) {
                commonResponseModel.value = CommonResponse().apply { this.error = true; message = error }
            }

        })

        return commonResponseModel
    }


}