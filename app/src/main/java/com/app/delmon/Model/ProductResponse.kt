package com.app.delmon.Model

data class ProductResponse(
    var `data`: List<Data?>? = null,
    var error: Boolean? = null,
    var totalCartCount: Int = 0,
    var message: String? = null
) {
    data class Data(
        var active: Int? = null,
        var categoryId: Int? = null,
        var createdAt: String? = null,
        var description: String? = null,
        var arDescription: String? = null,
        var id: Int? = null,
        var productId: Int? = null,
        var image: List<String?>? = null,
        var isStock: Int? = null,
        var name: String? = "",
        var arProductName: String? = "",
        var enProductName: String? = "",
        var noOfPieces: Int? = null,
        var price: Double = 0.0,
        var serves: String? = null,
        var type: String? = null,
        var updatedAt: String? = null,
        var weight: String? = "0",
        var quantity: Int = 0,
        var isFavorites: Int = 0,
        var vat: Int? = 0,
        var totprice:Double = price * quantity
    )
}