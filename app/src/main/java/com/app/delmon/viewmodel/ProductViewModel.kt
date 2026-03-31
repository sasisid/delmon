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
import com.app.delmon.utils.AppJson
import com.app.delmon.utils.Constants
import com.app.delmon.utils.postFromJson
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
                commonResponseModel.postFromJson(
                    jsonObject,
                    ProductDetailResponse::class.java,
                    AppJson.productDetailGson
                ) { err ->
                    ProductDetailResponse(error = true, message = err)
                }
            }

            override fun setErrorResponse(error: String) {
                commonResponseModel.value = ProductDetailResponse(error = true, message = error)
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
                commonResponseModel.postFromJson(jsonObject, AddCartResponse::class.java) { err ->
                    AddCartResponse().apply { error = true; massage = err }
                }
            }

            override fun setErrorResponse(error: String) {
                commonResponseModel.value = AddCartResponse().apply { this.error = true; massage = error }
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
                commonResponseModel.postFromJson(jsonObject, AddCartResponse::class.java) { err ->
                    AddCartResponse().apply { error = true; massage = err }
                }
            }

            override fun setErrorResponse(error: String) {
                commonResponseModel.value = AddCartResponse().apply { this.error = true; massage = error }
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
                commonResponseModel.postFromJson(jsonObject, CartResponseNew::class.java) { err ->
                    CartResponseNew().apply { error = true; message = err }
                }
            }

            override fun setErrorResponse(error: String) {
                commonResponseModel.value = CartResponseNew().apply { this.error = true; message = error }
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
                commonResponseModel.postFromJson(jsonObject, BasketNameResponse::class.java) { err ->
                    BasketNameResponse().apply { error = true; message = err }
                }
            }

            override fun setErrorResponse(error: String) {
                commonResponseModel.value = BasketNameResponse().apply { this.error = true; message = error }
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
                commonResponseModel.postFromJson(jsonObject, AdminBasketResponse::class.java) {
                    AdminBasketResponse(error = true)
                }
            }

            override fun setErrorResponse(error: String) {
                commonResponseModel.value = AdminBasketResponse(error = true)
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
                try {
                    Log.d("TAG", "setResponseSuccess: ${jsonObject.optJSONArray("productList")}")
                    val njsonObject = JSONObject()
                    njsonObject.put("error", jsonObject.optBoolean("error", false))
                    njsonObject.put("message", "")
                    val arr = jsonObject.optJSONArray("productList")
                    if (arr == null) {
                        commonResponseModel.value = ProductResponse().apply {
                            error = true
                            message = "Invalid product list"
                        }
                        return
                    }
                    njsonObject.put("data", arr)
                    commonResponseModel.postFromJson(njsonObject, ProductResponse::class.java) { err ->
                        ProductResponse().apply { error = true; message = err }
                    }
                } catch (e: Exception) {
                    commonResponseModel.value = ProductResponse().apply {
                        error = true
                        message = e.message
                    }
                }
            }

            override fun setErrorResponse(error: String) {
                commonResponseModel.value = ProductResponse().apply { this.error = true; message = error }
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
                commonResponseModel.postFromJson(jsonObject, CheckLoyalResponse::class.java) { err ->
                    CheckLoyalResponse().apply { error = true; massage = err }
                }
            }

            override fun setErrorResponse(error: String) {
                commonResponseModel.value = CheckLoyalResponse().apply { this.error = true; massage = error }
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
                commonResponseModel.postFromJson(jsonObject, CouponListModel::class.java) {
                    CouponListModel(success = false)
                }
            }

            override fun setErrorResponse(error: String) {
                commonResponseModel.value = CouponListModel(success = false)
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
        jsonObject.put("cartonDiscount", employeeCartonDiscount)

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
        if (Constants.User.usertype == "EMPLOYEE") {
            jsonObject.put("maxCartonDiscountPerDay", cartonDiscountedCount)
        }else{
            jsonObject.put("maxCartonDiscountPerDayUser", cartonDiscountedCount)
        }
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
