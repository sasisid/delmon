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
import org.json.JSONArray
import org.json.JSONObject

class ProductViewModel (application: Application) : AndroidViewModel(application) {

    var applicationIns: Application = application
    var sharedHelper: SharedHelper? = SharedHelper(applicationIns.applicationContext)
    val cartCount: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>(0)
    }
    val cartPrice: MutableLiveData<Double> by lazy {
        MutableLiveData<Double>(0.0)
    }
    private fun getApiParams(
        context: Context,
        jsonObject: JSONObject?,
        methodName: String
    ): ApiInput {

        val header: MutableMap<String, String> = HashMap()
        header[Constants.ApiKeys.LANG] = sharedHelper!!.language
        header["Authorization"] = "Bearer "+sharedHelper!!.token
        header["type"] = sharedHelper!!.appType.toString()
        if (sharedHelper!!.id != null ){
            header["userId"] = sharedHelper!!.id.toString()
        }

        Log.e("appSample", "Bearer: ${sharedHelper!!.token}")
        Log.e("appSample", "type: ${sharedHelper!!.appType}")
        Log.e("appSample", "userId: ${sharedHelper!!.id.toString()}")

        val apiInputs = ApiInput()
        apiInputs.context = context
        apiInputs.jsonObject = jsonObject
        apiInputs.headers = header
        apiInputs.url = methodName

        return apiInputs
    }

    fun getProductData(
        context: Context,
        productId: Int,
    ): LiveData<ProductDetailResponse> {

        val commonResponseModel: MutableLiveData<ProductDetailResponse> = MutableLiveData()
        Api.getMethod(getApiParams(
            context,
            null,
            UrlHelper.PRODUCTDETAIL+"/"+productId
        ), object : ApiResponseCallback {
            override fun setResponseSuccess(jsonObject: JSONObject) {
                Log.d("TAG", "sizeRecycData setResponseSuccess: $jsonObject")
                val gson = Gson()
                val response: ProductDetailResponse =  gson.fromJson(jsonObject.toString(), ProductDetailResponse::class.java)
                commonResponseModel.value = response
            }

            override fun setErrorResponse(error: String) {
                val response = ProductDetailResponse()
                response.error = true
                commonResponseModel.value = response
            }

        })

        return commonResponseModel
    }



    fun addToCart(
        context: Context,
        productId: Int,
        price: String,
    ): LiveData<AddCartResponse> {
        var jsonObject: JSONObject = JSONObject()
        jsonObject.put("productId", productId)
        jsonObject.put("price", price)
        val commonResponseModel: MutableLiveData<AddCartResponse> = MutableLiveData()
        Api.postMethod(getApiParams(
            context,
            jsonObject,
            UrlHelper.CART
        ), object : ApiResponseCallback {
            override fun setResponseSuccess(jsonObject: JSONObject) {
                Log.d("TAG", "setResponseSuccess: $jsonObject")
                val gson = Gson()
                val response: AddCartResponse =  gson.fromJson(jsonObject.toString(), AddCartResponse::class.java)
                commonResponseModel.value = response
            }

            override fun setErrorResponse(error: String) {
                val response = AddCartResponse()
                response.error = true
                commonResponseModel.value = response
            }

        })

        return commonResponseModel
    }

    fun updateCart(
        context: Context,
        cartId: Int,
        quantity: Int,
    ): LiveData<AddCartResponse> {
        var jsonObject: JSONObject = JSONObject()
        jsonObject.put("quantity", quantity)
        val commonResponseModel: MutableLiveData<AddCartResponse> = MutableLiveData()
        Api.postMethod(getApiParams(
            context,
            jsonObject,
            UrlHelper.CART+"/${cartId}"
        ), object : ApiResponseCallback {
            override fun setResponseSuccess(jsonObject: JSONObject) {
                Log.d("TAG", "setResponseSuccess: $jsonObject")
                val gson = Gson()
                val response: AddCartResponse =  gson.fromJson(jsonObject.toString(), AddCartResponse::class.java)
                commonResponseModel.value = response
            }

            override fun setErrorResponse(error: String) {
                val response = AddCartResponse()
                response.error = true
                commonResponseModel.value = response
            }

        })

        return commonResponseModel
    }

    fun updateReport(
        context: Context,
        comment:String
    ): LiveData<CommonResponse> {
        var jsonObject: JSONObject = JSONObject()
        jsonObject.put("comment", comment)
        val commonResponseModel: MutableLiveData<CommonResponse> = MutableLiveData()
        Api.postMethod(getApiParams(
            context,
            jsonObject,
            UrlHelper.REPORT
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

    fun getCart(
        context: Context,
    ): LiveData<CartResponseNew> {

        val commonResponseModel: MutableLiveData<CartResponseNew> = MutableLiveData()
        Api.getMethod(getApiParams(
            context,
            null,
            UrlHelper.CARTVIEW
        ), object : ApiResponseCallback {
            override fun setResponseSuccess(jsonObject: JSONObject) {
                Log.d("TAG", "setResponseSuccess: $jsonObject")
                val gson = Gson()
                val response: CartResponseNew =  gson.fromJson(jsonObject.toString(), CartResponseNew::class.java)
                commonResponseModel.value = response
            }

            override fun setErrorResponse(error: String) {
                val response = CartResponseNew()
                response.error = true
                commonResponseModel.value = response
            }

        })

        return commonResponseModel
    }



    fun createBasketName(
        context: Context,
        name: String,
    ): LiveData<CommonResponse> {

        val commonResponseModel: MutableLiveData<CommonResponse> = MutableLiveData()
        val jsonObject = JSONObject()
        jsonObject.put("name", name)
        Api.postMethod(getApiParams(
            context,
            jsonObject,
            UrlHelper.CREATEBASKET
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

    fun deleteBasketName(
        context: Context,
        basketId: Int,
    ): LiveData<CommonResponse> {

        val commonResponseModel: MutableLiveData<CommonResponse> = MutableLiveData()
        val jsonObject = JSONObject()
        jsonObject.put("basketId", basketId)
        Api.postMethod(getApiParams(
            context,
            null,
            UrlHelper.DELETEBASKET+basketId
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

    fun getBasketData(
        context: Context,
    ): LiveData<BasketNameResponse> {

        val commonResponseModel: MutableLiveData<BasketNameResponse> = MutableLiveData()
        Api.getMethod(getApiParams(
            context,
            null,
            UrlHelper.BASKETLIST
        ), object : ApiResponseCallback {
            override fun setResponseSuccess(jsonObject: JSONObject) {
                Log.d("TAG", "setResponseSuccess: $jsonObject")
                val gson = Gson()
                val response: BasketNameResponse =  gson.fromJson(jsonObject.toString(), BasketNameResponse::class.java)
                commonResponseModel.value = response
            }

            override fun setErrorResponse(error: String) {
                val response = BasketNameResponse()
                response.error = true
                commonResponseModel.value = response
            }

        })

        return commonResponseModel
    }
    fun getAdminBasketData(
        context: Context,
    ): LiveData<AdminBasketResponse> {

        val commonResponseModel: MutableLiveData<AdminBasketResponse> = MutableLiveData()
        Api.getMethod(getApiParams(
            context,
            null,
            UrlHelper.ADMINBASKET
        ), object : ApiResponseCallback {
            override fun setResponseSuccess(jsonObject: JSONObject) {
                Log.d("TAG", "setResponseSuccess: $jsonObject")
                val gson = Gson()
                val response: AdminBasketResponse =  gson.fromJson(jsonObject.toString(), AdminBasketResponse::class.java)
                commonResponseModel.value = response
            }

            override fun setErrorResponse(error: String) {
                val response = AdminBasketResponse()
                response.error = true
                commonResponseModel.value = response
            }

        })

        return commonResponseModel
    }

    fun createBasketproduct(
        context: Context,
        basketId: Int,
        productId: Int,
    ): LiveData<CommonResponse> {

        val commonResponseModel: MutableLiveData<CommonResponse> = MutableLiveData()
        val jsonObject = JSONObject()
        jsonObject.put("basketId", basketId)
        jsonObject.put("productId", productId)
        Api.postMethod(getApiParams(
            context,
            jsonObject,
            UrlHelper.CREATEBASKETPRODUCT
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

    fun deleteBasketProduct(
        context: Context,
        basketId: Int,
    ): LiveData<CommonResponse> {

        val commonResponseModel: MutableLiveData<CommonResponse> = MutableLiveData()
        val jsonObject = JSONObject()
        jsonObject.put("basketId", basketId)
        Api.postMethod(getApiParams(
            context,
            null,
            UrlHelper.DELETEBASKETPRODUCT+basketId
        ), object : ApiResponseCallback {
            override fun setResponseSuccess(jsonObject: JSONObject) {
                Log.d("TAG", "setResponseSuccess: $jsonObject")
                val gson = Gson()
                val response: CommonResponse =  gson.fromJson(jsonObject.toString(), CommonResponse::class.java)
                commonResponseModel.value = response
            }

            override fun setErrorResponse(error: String) {
                Log.d("TAG", "setErrorResponse: $error")

                val response = CommonResponse()
                response.error = true
                commonResponseModel.value = response
            }

        })

        return commonResponseModel
    }

    fun getBasketProductData(
        context: Context,
        basketId: Int
    ): LiveData<ProductResponse> {

        val commonResponseModel: MutableLiveData<ProductResponse> = MutableLiveData()
        val jsonObject = JSONObject()
        jsonObject.put("basketId", basketId)
        Api.getMethod(getApiParams(
            context,
            null,
            UrlHelper.BASKETPRODUCTLIST+"?basketId=${basketId}"
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
    fun getAdminBasketProductData(
        context: Context,
        basketId: Int
    ): LiveData<ProductResponse> {

        val commonResponseModel: MutableLiveData<ProductResponse> = MutableLiveData()
        val jsonObject = JSONObject()
        jsonObject.put("basketId", basketId)
        Api.getMethod(getApiParams(
            context,
            null,
            UrlHelper.ADMINBASKETPRODUCTLIST+"${basketId}"
        ), object : ApiResponseCallback {
            override fun setResponseSuccess(jsonObject: JSONObject) {
                Log.d("TAG", "setResponseSuccess: ${jsonObject.getJSONArray("productList")}")
                var njsonObject:JSONObject = JSONObject()
                njsonObject.put("error",jsonObject.getBoolean("error"))
                njsonObject.put("message","")
                njsonObject.put("data",jsonObject.getJSONArray("productList"))
                val gson = Gson()
                val response: ProductResponse =  gson.fromJson(njsonObject.toString(), ProductResponse::class.java)
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



    fun updateFavorites(
        context: Context,
        productId: Int,
        favourites: Int,
    ): LiveData<CommonResponse> {
        var jsonObject: JSONObject = JSONObject()
        jsonObject.put("productId", productId)
        jsonObject.put("isFavorites", favourites)
        val commonResponseModel: MutableLiveData<CommonResponse> = MutableLiveData()
        Api.postMethod(getApiParams(
            context,
            jsonObject,
            UrlHelper.UPDATEFAVORITES
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


    fun getFavoritesProductData(
        context: Context,
    ): LiveData<ProductResponse> {

        val commonResponseModel: MutableLiveData<ProductResponse> = MutableLiveData()
        val jsonObject = JSONObject()
        Api.getMethod(getApiParams(
            context,
            null,
            UrlHelper.FAVORITES
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

    fun basketToCart(
        context: Context,
        basketId: Int
    ): LiveData<CommonResponse> {

        val commonResponseModel: MutableLiveData<CommonResponse> = MutableLiveData()
        val jsonObject = JSONObject()
        Api.getMethod(getApiParams(
            context,
            null,
            UrlHelper.BASKETTOCART+basketId
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
    fun adminbasketToCart(
        context: Context,
        basketId: Int
    ): LiveData<CommonResponse> {

        val commonResponseModel: MutableLiveData<CommonResponse> = MutableLiveData()
        val jsonObject = JSONObject()
        Api.getMethod(getApiParams(
            context,
            null,
            UrlHelper.ADMINBASKETTOCART+"?productId="+basketId
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

    fun checkLoyaltyAmount(context: Context, point: Double,amount:Double): LiveData<CheckLoyalResponse> {
        val commonResponseModel: MutableLiveData<CheckLoyalResponse> = MutableLiveData()
        val jsonObject = JSONObject()
        Api.getMethod(getApiParams(
            context,
            null,
            UrlHelper.CHECK_LOYALTY+"totalLoyaltyPoint=$point&totalAmount=$amount"
        ), object : ApiResponseCallback {
            override fun setResponseSuccess(jsonObject: JSONObject) {
                Log.d("TAG", "setResponseSuccess: $jsonObject")
                val gson = Gson()
                val response: CheckLoyalResponse =  gson.fromJson(jsonObject.toString(), CheckLoyalResponse::class.java)
                commonResponseModel.value = response
            }

            override fun setErrorResponse(error: String) {
                val response = CheckLoyalResponse()
                response.error = true
                commonResponseModel.value = response
            }

        })

        return commonResponseModel
    }

    fun getCouponList(
        context: Context
    ): LiveData<CouponListModel> {
        val commonResponseModel: MutableLiveData<CouponListModel> = MutableLiveData()
        Api.getMethod(getApiParams(
            context,
            null,
            UrlHelper.COUPON_LIST
        ), object : ApiResponseCallback {
            override fun setResponseSuccess(jsonObject: JSONObject) {
                Log.d("TAG", "setResponseSuccess: $jsonObject")
                val gson = Gson()
                val response: CouponListModel =  gson.fromJson(jsonObject.toString(), CouponListModel::class.java)
                commonResponseModel.value = response
            }

            override fun setErrorResponse(error: String) {
                val response = CouponListModel()
                response.success = false
                commonResponseModel.value = response
            }

        })

        return commonResponseModel
    }


    fun placeOrder(
        context: Context,
        cartonDiscountedCount:Int,
        cartId: JSONArray,
        orderType: String,
        orderData:JSONArray,
        paymentType:String,
        grandTotal:Double,
        vat:Double,
        pickupAddr:String,
        notes:String,
        loyaltyAmount:Double,
        isLoyaltyApply:Int,
        deliveryAddr:String,
        lat:String,
        lng:String,
        couponId:String,
        couponAmount:Double,
        deliveryCharge:Double,
        deliveryDate:String,
        userCartonDiscount: Int,
        employeeCartonDiscount: Int
    ): LiveData<CommonResponse> {
        var jsonObject: JSONObject = JSONObject()
        jsonObject.put("cartId", cartId.toString())

        // Todo - Changed 28-07-24. Whatever value, i will send data.
        /*if (cartonDiscountedCount != 0) {
            jsonObject.put("cartonDiscount", cartonDiscountedCount)
        }*/
        jsonObject.put("cartonDiscount", cartonDiscountedCount)

        jsonObject.put("order", orderData.toString())
        jsonObject.put("deliveryType", orderType)
        jsonObject.put("netAmount", grandTotal)
        jsonObject.put("paymentTypeId", paymentType)
        jsonObject.put("pickupAddress", pickupAddr)
        jsonObject.put("deliveryAddress", deliveryAddr)
//        jsonObject.put("deliveryOrderDate", "")
        jsonObject.put("deliveryOrderDate", deliveryDate)
        jsonObject.put("latitude", lat)
        jsonObject.put("longitude", lng)
        jsonObject.put("couponId", couponId)
        jsonObject.put("couponAmount", couponAmount)
        jsonObject.put("isLoyaltyPointApply", isLoyaltyApply)
        jsonObject.put("orderReferenceId", "123456")
        jsonObject.put("vat", vat)
        jsonObject.put("deliveryCharge", deliveryCharge)
        jsonObject.put("deliveryDate", deliveryDate)
        jsonObject.put("deliveryNotes", notes)
        jsonObject.put("LoyaltyAmount", loyaltyAmount)
        jsonObject.put("userCartonDiscount", userCartonDiscount)
        jsonObject.put("employeeCartonDiscount", employeeCartonDiscount)
        Log.d("dxcvb",""+jsonObject)
        val commonResponseModel: MutableLiveData<CommonResponse> = MutableLiveData()
        Api.postMethod(getApiParams(
            context,
            jsonObject,
            UrlHelper.PLACE_ORDER
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
}
