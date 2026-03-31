package com.app.delmon.Model

data class CartResponse(
    var address: String? = null,
    var `data`: List<Data?>? = null,
    var error: Boolean? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var loyaltyPoint: Int? = null,
    var loyaltyPointDiscount: Int? = null,
    var totalCartCount: String? = null,
    var vat: Int? = null,
    var walletBalance: Int? = null
) {
    data class Data(
        var arProductName: String? = null,
        var categoryId: Int? = null,
        var createdAt: String? = null,
        var enProductName: String? = null,
        var id: Int? = null,
        var image: List<String?>? = null,
        var name: String? = null,
        var price: Double? = null,
        var productId: Int? = null,
        var quantity: Int? = null,
        var type: String? = null,
        var updatedAt: String? = null,
        var userId: Int? = null,
        var weight: String? = null
    )
}