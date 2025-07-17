package com.app.delmon.Model

import com.google.gson.annotations.SerializedName

data class CartResponseNew(
    var address: Address? = null,
    var adminDate: String = "",
    var `data`: List<Data?>? = null,
    var error: Boolean? = null,
    var loyaltyPoint: Double = 0.0,
    var loyaltyPointDiscount: Int? = null,
    var totalCartCount: String? = "0",
    var message: String? = null,
    var vat: Int? = null,
    var cartonDiscount: Int = 0,
    var maxCartonDiscountPerDay: Int = 0,
    var defaultCartonDiscount: Int = 0,
    var defaultMaxCartonDiscountPerDayUser: Int = 0,
    var defaultMaxCartonDiscountPerDayEmployee: Int = 0,
    var canCalendarShowForDelivery: Boolean? = true,
    var maxDeliveryDateCanChoose: Int = 0,
    var walletBalance: Double = 0.0,
    var isCardPayment: Int = 0,
    var isCod: Int = 0,
    var isSelfPickup: Int = 0,
    var isDelivery: Int = 0,
    @SerializedName("holidayList")
    val holidayList: List<String> = listOf()
) {
    data class Address(
        var address: String? = null,
        var buildingName: String? = null,
        var landmark: String? = null,
        var latitude: String? = null,
        var longitude: String? = null,
        var pin: Int? = null
    )

    data class Data(
        var arProductName: String? = null,
        var categoryId: Int? = null,
        var categoryName: String? = null,
        var createdAt: String? = null,
        var enProductName: String? = null,
        var id: Int? = null,
        var image: List<String?>? = null,
        var name: String? = null,
        var price: Double = 0.0,
        var totprice: Double = 0.0,
        var vatPrice: Double = 0.0,
        var productId: Int? = null,
        var quantity: Int = 0,
        var type: String? = null,
        var updatedAt: String? = null,
        var userId: Int? = null,
        var vat: Int = 0,
        var weight: String? = null,
        var noOfPieces: Int = 0,
        var cartonActive: Int = 0,
        var piecesActive: Int = 0,
        var productPriceListId: Int = 0,
        var stock: Int = 0,
    ){
        constructor() : this(vat = 0, weight = "",noOfPieces=0,piecesActive=0,cartonActive=0,quantity=0)// this constructor is an explicit
        // "empty" constructor, as seen by Java.
    }
}