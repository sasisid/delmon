package com.app.delmon.network

import com.app.delmon.Model.PaymentSession
import com.app.delmon.R
import com.app.delmon.app.AppController


object UrlHelper {

// private const val BASE = "http://15.184.181.76:8080/"  // Development
 private const val BASE = "https://api.dawajen.bh/"  // Production
 //private const val BASE_URL = BASE + "/api/v1/users/"
 private const val BASE_URL = BASE + "v1/user/"
 private const val TECHNICIAN_BASE_URL = BASE + "v1/technician/"
 const val SOCKETURL = "http://13.55.52.81:8080/";

 private const val GOOGLE_API_BASE_URL = "https://maps.googleapis.com/maps/api/"
 const val GOOGLE_API_DIRECTION_BASE_URL = GOOGLE_API_BASE_URL + "directions/json?"
 const val GOOGLE_API_AUTOCOMPLETE_BASE_URL = GOOGLE_API_BASE_URL + "place/autocomplete/json?"
 const val GOOGLE_API_PLACE_DETAILS_BASE_URL = GOOGLE_API_BASE_URL + "place/details/json?"
 const val GOOGLE_API_STATIC_MAP_BASE_URL = GOOGLE_API_BASE_URL + "staticmap?"
 private const val GOOGLE_API_GEOCODE_BASE_URL = GOOGLE_API_BASE_URL + "geocode/json?"

 public const val  PERMISSION_REQUEST_CODE = 200;
 public const val GPS_REQUEST_CODE = 201;

  const val IS_GPS = "IsGps"


 const val HOME_DATA = BASE_URL + "home"
 const val PRODUCT = BASE_URL + "product"
 const val TRENDING_PRODUCT = BASE_URL + "trendingProduct"
 const val PaymentSession = "https://credimax.gateway.mastercard.com/api/rest/version/75/merchant/E15251950/session"
 const val CART = BASE_URL + "cart"
 const val REPORT = BASE_URL + "report"
 const val UPDATEFAVORITES = BASE_URL + "favorites"
 const val FAVORITES = BASE_URL + "favorites/get"
 const val CARTVIEW = BASE_URL + "cart/view"
 const val BASKETLIST = BASE_URL + "basket/list"
 const val ADMINBASKET = BASE_URL + "product/basket"
 const val CREATEBASKET = BASE_URL + "basket"
 const val DELETEBASKET = BASE_URL + "basket/delete/"
 const val DELETEBASKETPRODUCT = BASE_URL + "basketProductList/delete/"
 const val CREATEBASKETPRODUCT = BASE_URL + "basketProductList"
 const val BASKETPRODUCTLIST = BASE_URL + "basketProductList/list"
 const val ADMINBASKETPRODUCTLIST = BASE_URL + "basket/productDetail/"
 const val BASKETTOCART = BASE_URL + "basketToCart/"
 const val ADMINBASKETTOCART = BASE_URL + "adminBasketToCart/"
 const val PRODUCTDETAIL = BASE_URL + "productDetail"
 const val CATEGORY = BASE_URL + "category"
 const val SUBCATEGORY = BASE_URL + "subCategory"
 const val RECIPIES = BASE_URL + "recipies"
 const val CHECKUSER = BASE_URL + "auth/sendOtp"
 const val VERIFYOTP = BASE_URL + "auth/verifyOtp"
 const val CREATEUSER = BASE_URL + "auth/createUser"
 const val USER = BASE_URL
 const val UPDATEUSER = BASE_URL + "update"
 const val PIN = BASE_URL + "pin"
 const val ZONE = BASE_URL + "zone"
 const val AREA = BASE_URL + "findArea?zoneId="
 const val GETADDRESS = BASE_URL+"address/list"
 const val URL = BASE_URL+"url"
 const val ADDADDRESS = BASE_URL + "address"
 const val DELETEADDRESS = BASE_URL + "address/delete"
 const val ORDER_LIST = BASE_URL + "order?orderType="
 const val GET_ORDER = BASE_URL + "order/getOrderDetails?orderId="
 const val CHECK_LOYALTY = BASE_URL + "applyLoyaltyPoints?"
 const val COUPON_LIST = BASE_URL + "coupon"
 const val PLACE_ORDER = BASE_URL + "order"
 const val RE_ORDER = BASE_URL + "order/reOrder/"
 const val LOYALTY = BASE_URL + "loyaltyPoints"
 const val WALLET = BASE_URL + "wallet"
 const val ADD_WALLET = BASE_URL + "wallet/add"
 const val SPINNER_DATA = BASE_URL + "spinAndWin"
 const val UpdateWINLIST = BASE_URL + "spinAndWinList"


 fun getAddress(
  latitude: Double,
  longitude: Double
 ): String {
  val lat = latitude.toString()
  val lngg = longitude.toString()
  return (GOOGLE_API_GEOCODE_BASE_URL + "latlng=" + lat + ","
          + lngg + "&sensor=true&key=" + AppController.getInstance().resources.getString(
   R.string.map_api_key
  ))
 }

}