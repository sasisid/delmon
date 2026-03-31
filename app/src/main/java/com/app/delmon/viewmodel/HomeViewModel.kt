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
import com.app.delmon.utils.postFromJson
import org.json.JSONObject

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    var applicationIns: Application = application
    var sharedHelper: SharedHelper? = SharedHelper(applicationIns.applicationContext)


    private fun getApiParams(
        context: Context,
        jsonObject: JSONObject?,
        methodName: String
    ): ApiInput {
        val header: MutableMap<String, String> = HashMap()
        header[Constants.ApiKeys.LANG] = sharedHelper!!.language
        header["Authorization"] = "Bearer "+sharedHelper!!.token
        if (sharedHelper!!.id != null ) {
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
                commonResponseModel.postFromJson(jsonObject, ProductResponse::class.java) { err ->
                    ProductResponse().apply { error = true; message = err }
                }
            }

            override fun setErrorResponse(error: String) {
                commonResponseModel.value = ProductResponse().apply { this.error = true; message = error }
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
                commonResponseModel.postFromJson(jsonObject, TrendingProductResponse::class.java) { err ->
                    TrendingProductResponse(error = true, message = err)
                }
            }

            override fun setErrorResponse(error: String) {
                commonResponseModel.value = TrendingProductResponse(error = true, message = error)
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
                commonResponseModel.postFromJson(jsonObject, PaymentSession::class.java) {
                    PaymentSession(result = "FAILURE")
                }
            }

            override fun setErrorResponse(error: String) {
                commonResponseModel.value = PaymentSession(result = "FAILURE")
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
                commonResponseModel.postFromJson(jsonObject, ProductResponse::class.java) { err ->
                    ProductResponse().apply { error = true; message = err }
                }
            }

            override fun setErrorResponse(error: String) {
                commonResponseModel.value = ProductResponse().apply { this.error = true; message = error }
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
                commonResponseModel.postFromJson(jsonObject, CategoryResponse::class.java) { err ->
                    CategoryResponse(error = true, message = err)
                }
            }

            override fun setErrorResponse(error: String) {
                commonResponseModel.value = CategoryResponse(error = true, message = error)
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
                commonResponseModel.postFromJson(jsonObject, HomeResponse::class.java) { err ->
                    HomeResponse(error = true, message = err)
                }
            }

            override fun setErrorResponse(error: String) {
                commonResponseModel.value = HomeResponse(error = true, message = error)
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
                commonResponseModel.postFromJson(jsonObject, SpinnerListData::class.java) {
                    SpinnerListData(success = false)
                }
            }

            override fun setErrorResponse(error: String) {
                commonResponseModel.value = SpinnerListData(success = false)
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
                commonResponseModel.postFromJson(jsonObject, SpinnerListData::class.java) {
                    SpinnerListData(success = false)
                }
            }

            override fun setErrorResponse(error: String) {
                commonResponseModel.value = SpinnerListData(success = false)
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
                commonResponseModel.postFromJson(jsonObject, SubCategoryResponse::class.java) { err ->
                    SubCategoryResponse().apply { error = true; message = err }
                }
            }

            override fun setErrorResponse(error: String) {
                commonResponseModel.value = SubCategoryResponse().apply { this.error = true; message = error }
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
                commonResponseModel.postFromJson(jsonObject, RecipesResponse::class.java) { err ->
                    RecipesResponse().apply { error = true; message = err }
                }
            }

            override fun setErrorResponse(error: String) {
                commonResponseModel.value = RecipesResponse().apply { this.error = true; message = error }
            }

        })

        return commonResponseModel
    }

    fun getOrderList(context: Context,type:String): LiveData<OrderListResponse> {
        val commonResponseModel: MutableLiveData<OrderListResponse> = MutableLiveData()
        Api.getMethod(getApiParams(context, null, UrlHelper.ORDER_LIST+type), object : ApiResponseCallback {
            override fun setResponseSuccess(jsonObject: JSONObject) {
                Log.d("TAG", "setResponseSuccess: $jsonObject")
                commonResponseModel.postFromJson(jsonObject, OrderListResponse::class.java) {
                    OrderListResponse(success = false)
                }
            }
            override fun setErrorResponse(error: String) {
                commonResponseModel.value = OrderListResponse(success = false)
            }

        })
        return commonResponseModel
    }

    fun  getWalletList(context: Context): LiveData<WalletResponse> {
        val commonResponseModel: MutableLiveData<WalletResponse> = MutableLiveData()
        Api.getMethod(getApiParams(context, null, UrlHelper.WALLET), object : ApiResponseCallback {
            override fun setResponseSuccess(jsonObject: JSONObject) {
                Log.d("TAG", "setResponseSuccess: $jsonObject")
                commonResponseModel.postFromJson(jsonObject, WalletResponse::class.java) {
                    WalletResponse(error = true)
                }
            }
            override fun setErrorResponse(error: String) {
                commonResponseModel.value = WalletResponse(error = true)
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
                commonResponseModel.postFromJson(jsonObject, AddWalletResponse::class.java) { err ->
                    AddWalletResponse().apply { error = true; message = err }
                }
            }
            override fun setErrorResponse(error: String) {
                commonResponseModel.value = AddWalletResponse().apply { this.error = true; message = error }
            }

        })
        return commonResponseModel
    }
    fun gePointList(context: Context): LiveData<WalletResponse> {
        val commonResponseModel: MutableLiveData<WalletResponse> = MutableLiveData()
        Api.getMethod(getApiParams(context, null, UrlHelper.LOYALTY), object : ApiResponseCallback {
            override fun setResponseSuccess(jsonObject: JSONObject) {
                Log.d("TAG", "setResponseSuccess: $jsonObject")
                commonResponseModel.postFromJson(jsonObject, WalletResponse::class.java) {
                    WalletResponse(error = true)
                }
            }
            override fun setErrorResponse(error: String) {
                commonResponseModel.value = WalletResponse(error = true)
            }

        })
        return commonResponseModel
    }

    fun getOrder(context: Context,id:String): LiveData<OrderListResponse> {
        val commonResponseModel: MutableLiveData<OrderListResponse> = MutableLiveData()
        Api.getMethod(getApiParams(context, null, UrlHelper.GET_ORDER+id), object : ApiResponseCallback {
            override fun setResponseSuccess(jsonObject: JSONObject) {
                Log.d("TAG", "setResponseSuccess: $jsonObject")
                commonResponseModel.postFromJson(jsonObject, OrderListResponse::class.java) {
                    OrderListResponse(success = false)
                }
            }
            override fun setErrorResponse(error: String) {
                commonResponseModel.value = OrderListResponse(success = false)
            }

        })
        return commonResponseModel
    }


}