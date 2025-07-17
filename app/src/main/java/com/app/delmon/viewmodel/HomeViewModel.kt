package com.app.delmon.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.delmon.Model.*
import com.app.delmon.Session.SharedHelper
import com.app.delmon.interfaces.ApiResponseCallback
import com.app.delmon.network.Api
import com.app.delmon.network.ApiInput
import com.app.delmon.network.UrlHelper
import com.app.delmon.utils.Constants
import com.google.gson.Gson
import org.json.JSONObject

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    var applicationIns: Application = application
    var sharedHelper: SharedHelper? = SharedHelper(applicationIns.applicationContext)


    private fun getApiParams(
        context: Context,
        jsonObject: JSONObject?,
        methodName: String
    ): ApiInput {
        Log.e("appSample", "Token: ${sharedHelper!!.token}")
        val header: MutableMap<String, String> = HashMap()
        header[Constants.ApiKeys.LANG] = sharedHelper!!.language
        header["Authorization"] = "Bearer "+sharedHelper!!.token
        if (sharedHelper!!.id != null ) {
            header["userId"] = sharedHelper!!.id.toString()
        }
        header["type"] = sharedHelper!!.appType.toString()

        Log.e("appSample", "Token: " + sharedHelper!!.token)
        Log.e("appSample", "UserId: " + sharedHelper!!.id.toString())
        Log.e("appSample", "Type: " + sharedHelper!!.appType)

        val apiInputs = ApiInput()
        apiInputs.context = context
        apiInputs.jsonObject = jsonObject
        apiInputs.headers = header
        apiInputs.url = methodName

        return apiInputs
    }

    fun getProductData(
        context: Context,
        catId: Int,
    ): LiveData<ProductResponse> {

        val commonResponseModel: MutableLiveData<ProductResponse> = MutableLiveData()
        Api.getMethod(getApiParams(
            context,
            null,
            UrlHelper.PRODUCT+"?categoryId="+catId
        ), object : ApiResponseCallback {
            override fun setResponseSuccess(jsonObject: JSONObject) {
                Log.d("TAG", "setResponseSuccess: $jsonObject")
                val gson = Gson()
                val response: ProductResponse =  gson.fromJson(jsonObject.toString(), ProductResponse::class.java)
                commonResponseModel.value = response
            }

            override fun setErrorResponse(error: String) {
                val response = ProductResponse()
                response.error = true
                commonResponseModel.value = response
            }

        })

        return commonResponseModel
    }

    fun getTrendingProductData(
        context: Context,
    ): LiveData<TrendingProductResponse> {

        val commonResponseModel: MutableLiveData<TrendingProductResponse> = MutableLiveData()
        Api.getMethod(getApiParams(
            context,
            null,
            UrlHelper.TRENDING_PRODUCT
        ), object : ApiResponseCallback {
            override fun setResponseSuccess(jsonObject: JSONObject) {
                Log.d("TAG", "setResponseSuccess: $jsonObject")
                val gson = Gson()
                val response: TrendingProductResponse =  gson.fromJson(jsonObject.toString(), TrendingProductResponse::class.java)
                commonResponseModel.value = response
            }

            override fun setErrorResponse(error: String) {
                val response = TrendingProductResponse()
                response.error = true
                commonResponseModel.value = response
            }

        })

        return commonResponseModel
    }

    fun getsessionData(
        context: Context,
    ): LiveData<PaymentSession> {

        val commonResponseModel: MutableLiveData<PaymentSession> = MutableLiveData()
        Api.getMethod(getApiParams(
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

    fun getSearchProductData(
        context: Context,
        text:String
    ): LiveData<ProductResponse> {

        val commonResponseModel: MutableLiveData<ProductResponse> = MutableLiveData()
        Api.getMethod(getApiParams(
            context,
            null,
            UrlHelper.PRODUCT+"?searchValue="+text
        ), object : ApiResponseCallback {
            override fun setResponseSuccess(jsonObject: JSONObject) {
                Log.d("TAG", "setResponseSuccess: $jsonObject")
                val gson = Gson()
                val response: ProductResponse =  gson.fromJson(jsonObject.toString(), ProductResponse::class.java)
                commonResponseModel.value = response
            }

            override fun setErrorResponse(error: String) {
                val response = ProductResponse()
                response.error = true
                commonResponseModel.value = response
            }

        })

        return commonResponseModel
    }

    fun getCategoryData(
        context: Context,
    ): LiveData<CategoryResponse> {

        val commonResponseModel: MutableLiveData<CategoryResponse> = MutableLiveData()
        Api.getMethod(getApiParams(
            context,
            null,
            UrlHelper.CATEGORY
        ), object : ApiResponseCallback {
            override fun setResponseSuccess(jsonObject: JSONObject) {
                Log.d("TAG", "setResponseSuccess: $jsonObject")
                val gson = Gson()
                val response: CategoryResponse =  gson.fromJson(jsonObject.toString(), CategoryResponse::class.java)
                commonResponseModel.value = response
            }

            override fun setErrorResponse(error: String) {
                val response = CategoryResponse()
                response.error = true
                commonResponseModel.value = response
            }

        })

        return commonResponseModel
    }

    fun getHomeData(
        context: Context,
    ): LiveData<HomeResponse> {

        val commonResponseModel: MutableLiveData<HomeResponse> = MutableLiveData()
        Api.getMethod(getApiParams(
            context,
            null,
            UrlHelper.HOME_DATA
        ), object : ApiResponseCallback {
            override fun setResponseSuccess(jsonObject: JSONObject) {
                Log.d("TAG", "setResponseSuccess: $jsonObject")
                Log.e("appSample", "homeDataResponse: $jsonObject")
                val gson = Gson()
                val response: HomeResponse =
                    gson.fromJson(jsonObject.toString(), HomeResponse::class.java)
                commonResponseModel.value = response
            }

            override fun setErrorResponse(error: String) {
                val response = HomeResponse()
                response.error = true
                commonResponseModel.value = response
            }

        })
        return commonResponseModel
    }

    fun getSpinnerData(
        context: Context,
    ): LiveData<SpinnerListData> {

        val commonResponseModel: MutableLiveData<SpinnerListData> = MutableLiveData()
        Api.getMethod(getApiParams(
            context,
            null,
            UrlHelper.SPINNER_DATA
        ), object : ApiResponseCallback {
            override fun setResponseSuccess(jsonObject: JSONObject) {
                Log.d("TAG", "setResponseSuccess: $jsonObject")
                val gson = Gson()
                val response: SpinnerListData =
                    gson.fromJson(jsonObject.toString(), SpinnerListData::class.java)
                commonResponseModel.value = response
            }

            override fun setErrorResponse(error: String) {
                val response = SpinnerListData()
                response.success = false
                commonResponseModel.value = response
            }

        })

        return commonResponseModel
    }

    fun updateWinData(
        context: Context,
        spinId:String
    ): LiveData<SpinnerListData> {
        var jsonObject: JSONObject = JSONObject()
        jsonObject.put("spinId", spinId)
        jsonObject.put("userId", sharedHelper!!.id.toString())
        val commonResponseModel: MutableLiveData<SpinnerListData> = MutableLiveData()
        Api.postMethod(getApiParams(
            context,
            jsonObject,
            UrlHelper.UpdateWINLIST
        ), object : ApiResponseCallback {
            override fun setResponseSuccess(jsonObject: JSONObject) {
                Log.d("TAG", "setResponseSuccess: $jsonObject")
                val gson = Gson()
                val response: SpinnerListData =
                    gson.fromJson(jsonObject.toString(), SpinnerListData::class.java)
                commonResponseModel.value = response
            }

            override fun setErrorResponse(error: String) {
                val response = SpinnerListData()
                response.success = false
                commonResponseModel.value = response
            }

        })

        return commonResponseModel
    }

    fun getSubCategoryData( context: Context,catId: Int
    ): LiveData<SubCategoryResponse> {

        val commonResponseModel: MutableLiveData<SubCategoryResponse> = MutableLiveData()
        Api.getMethod(getApiParams(
            context,
            null,
            UrlHelper.SUBCATEGORY+"?categoryId=${catId}"
        ), object : ApiResponseCallback {
            override fun setResponseSuccess(jsonObject: JSONObject) {
                Log.d("TAG", "setResponseSuccess: $jsonObject")
                val gson = Gson()
                val response: SubCategoryResponse =  gson.fromJson(jsonObject.toString(), SubCategoryResponse::class.java)
                commonResponseModel.value = response
            }

            override fun setErrorResponse(error: String) {
                val response = SubCategoryResponse()
                response.error = true
                commonResponseModel.value = response
            }

        })

        return commonResponseModel
    }


    fun orderupdateStatus(
        context: Context,
        orderId: String,
        status: String,
    ): LiveData<CommonResponse> {
        var jsonObject: JSONObject = JSONObject()
        jsonObject.put("orderStatus", status)
        val commonResponseModel: MutableLiveData<CommonResponse> = MutableLiveData()
        Api.postMethod(getApiParams(
            context,
            jsonObject,
            UrlHelper.PLACE_ORDER+"/${orderId}"
        ), object : ApiResponseCallback {
            override fun setResponseSuccess(jsonObject: JSONObject) {
                Log.d("TAG", "setResponseSuccess: $jsonObject")
                val gson = Gson()
                val response: CommonResponse =  gson.fromJson(jsonObject.toString(), CommonResponse::class.java)
                commonResponseModel.value = response
            }

            override fun setErrorResponse(error: String) {
                val response = CommonResponse()
                response.error = true
                commonResponseModel.value = response
            }

        })

        return commonResponseModel
    }

    fun reOrder(
        context: Context,
        orderId: String,
    ): LiveData<CommonResponse> {
        var jsonObject: JSONObject = JSONObject()
        val commonResponseModel: MutableLiveData<CommonResponse> = MutableLiveData()
        Api.getMethod(getApiParams(
            context,
            null,
            UrlHelper.RE_ORDER+"${orderId}"
        ), object : ApiResponseCallback {
            override fun setResponseSuccess(jsonObject: JSONObject) {
                Log.d("TAG", "setResponseSuccess: $jsonObject")
                val gson = Gson()
                val response: CommonResponse =  gson.fromJson(jsonObject.toString(), CommonResponse::class.java)
                commonResponseModel.value = response
            }

            override fun setErrorResponse(error: String) {
                val response = CommonResponse()
                response.error = true
                commonResponseModel.value = response
            }

        })

        return commonResponseModel
    }

    fun getRecipes( context: Context,catId:Int
    ): LiveData<RecipesResponse> {

        val commonResponseModel: MutableLiveData<RecipesResponse> = MutableLiveData()
        Api.getMethod(getApiParams(
            context,
            null,
            UrlHelper.RECIPIES+"?categoryId="+catId
        ), object : ApiResponseCallback {
            override fun setResponseSuccess(jsonObject: JSONObject) {
                Log.d("TAG", "setResponseSuccess: $jsonObject")
                val gson = Gson()
                val response: RecipesResponse =  gson.fromJson(jsonObject.toString(), RecipesResponse::class.java)
                commonResponseModel.value = response
            }

            override fun setErrorResponse(error: String) {
                val response = RecipesResponse()
                response.error = true
                commonResponseModel.value = response
            }

        })

        return commonResponseModel
    }

    fun getOrderList(context: Context,type:String): LiveData<OrderListResponse> {
        val commonResponseModel: MutableLiveData<OrderListResponse> = MutableLiveData()
        Api.getMethod(getApiParams(context, null, UrlHelper.ORDER_LIST+type), object : ApiResponseCallback {
            override fun setResponseSuccess(jsonObject: JSONObject) {
                Log.d("TAG", "setResponseSuccess: $jsonObject")
                val response: OrderListResponse =  Gson().fromJson(jsonObject.toString(), OrderListResponse::class.java)
                commonResponseModel.value = response
            }
            override fun setErrorResponse(error: String) {
                val response = OrderListResponse()
                response.success = false
                commonResponseModel.value = response
            }

        })
        return commonResponseModel
    }

    fun  getWalletList(context: Context): LiveData<WalletResponse> {
        val commonResponseModel: MutableLiveData<WalletResponse> = MutableLiveData()
        Api.getMethod(getApiParams(context, null, UrlHelper.WALLET), object : ApiResponseCallback {
            override fun setResponseSuccess(jsonObject: JSONObject) {
                Log.d("TAG", "setResponseSuccess: $jsonObject")
                val response: WalletResponse =  Gson().fromJson(jsonObject.toString(), WalletResponse::class.java)
                commonResponseModel.value = response
            }
            override fun setErrorResponse(error: String) {
                val response = WalletResponse()
                response.error = true
                commonResponseModel.value = response
            }

        })
        return commonResponseModel
    }

    fun addWalletAmount(context: Context,amount:Double): LiveData<AddWalletResponse>{
        val commonResponseModel: MutableLiveData<AddWalletResponse> = MutableLiveData()
        var jsonObject: JSONObject = JSONObject()
        jsonObject.put("amount", amount)
        jsonObject.put("paymentType", "card")
        jsonObject.put("type", "ADD")
        jsonObject.put("orderId", "")

        Api.postMethod(getApiParams(context, jsonObject, UrlHelper.ADD_WALLET), object : ApiResponseCallback {
            override fun setResponseSuccess(jsonObject: JSONObject) {
                Log.d("TAG", "setResponseSuccess: $jsonObject")
                val response: AddWalletResponse =  Gson().fromJson(jsonObject.toString(), AddWalletResponse::class.java)
                commonResponseModel.value = response
            }
            override fun setErrorResponse(error: String) {
                val response = AddWalletResponse()
                response.error = true
                commonResponseModel.value = response
            }

        })
        return commonResponseModel
    }
    fun gePointList(context: Context): LiveData<WalletResponse> {
        val commonResponseModel: MutableLiveData<WalletResponse> = MutableLiveData()
        Api.getMethod(getApiParams(context, null, UrlHelper.LOYALTY), object : ApiResponseCallback {
            override fun setResponseSuccess(jsonObject: JSONObject) {
                Log.d("TAG", "setResponseSuccess: $jsonObject")
                val response: WalletResponse =  Gson().fromJson(jsonObject.toString(), WalletResponse::class.java)
                commonResponseModel.value = response
            }
            override fun setErrorResponse(error: String) {
                val response = WalletResponse()
                response.error = true
                commonResponseModel.value = response
            }

        })
        return commonResponseModel
    }

    fun getOrder(context: Context,id:String): LiveData<OrderListResponse> {
        val commonResponseModel: MutableLiveData<OrderListResponse> = MutableLiveData()
        Api.getMethod(getApiParams(context, null, UrlHelper.GET_ORDER+id), object : ApiResponseCallback {
            override fun setResponseSuccess(jsonObject: JSONObject) {
                Log.d("TAG", "setResponseSuccess: $jsonObject")
                val response: OrderListResponse =  Gson().fromJson(jsonObject.toString(), OrderListResponse::class.java)
                commonResponseModel.value = response
            }
            override fun setErrorResponse(error: String) {
                val response = OrderListResponse()
                response.success = false
                commonResponseModel.value = response
            }

        })
        return commonResponseModel
    }


}