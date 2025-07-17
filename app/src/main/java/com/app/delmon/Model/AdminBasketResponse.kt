package com.app.delmon.Model

data class AdminBasketResponse(
    var `data`: List<Data?>? = null,
    var error: Boolean? = null
) {
    data class Data(
        var active: Int? = null,
        var arProductName: String? = null,
        var basketProductList: List<Int?>? = null,
        var cartonActive: Int? = null,
        var categoryId: Int? = null,
        var createdAt: String? = null,
        var description: String? = null,
        var enProductName: String? = null,
        var id: Int? = null,
        var image: List<String?>? = null,
        var isBasket: Int? = null,
        var isFavorites: Int? = null,
        var isStock: Int? = null,
        var mostWantedProduct: Int? = null,
        var name: String? = "",
        var newProduct: Int? = null,
        var noOfPieces: Any? = null,
        var offers: Int? = null,
        var parentId: Int? = null,
        var piecesActive: Int? = null,
        var price: Int? = null,
        var recipiesId: List<Int?>? = null,
        var serves: Any? = null,
        var soldType: Int? = null,
        var subDescription: Any? = null,
        var type: String? = null,
        var updatedAt: String? = null,
        var weight: Any? = null
    )
}