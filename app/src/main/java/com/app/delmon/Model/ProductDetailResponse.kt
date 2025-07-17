package com.app.delmon.Model

data class ProductDetailResponse(
    var `data`: Data? = null,
    var error: Boolean? = null,
    var message: String? = null
) {
    data class Data(
        var active: Int? = null,
        var arProductName: String? = null,
        var basketProductList: Any? = null,
        var cartId: Int? = 0,
        var cartonActive: Int? = 0,
        var categoryId: Int? = null,
        var categoryName: String? = null,
        var createdAt: String? = null,
        var description: String? = null,
        var enProductName: String? = null,
        var id: Int? = null,
        var image: List<String?>? = null,
        var isBasket: Int? = null,
        var isFavorites: Int? = 0,
        var isStock: Int? = null,
        var mostWantedProduct: Int? = null,
        var name: String? = null,
        var newProduct: Int? = null,
        var noOfPieces: Any? = null,
        var offers: Int? = null,
        var parentId: Int? = null,
        var piecesActive: Int? = 0,
        var price: Double? = 0.0,
        var quantity: Int? = 0,
        var recipiesId: List<Int?>? = null,
        var recipiesList: List<Recipies?>? = null,
        var relatedProduct: List<RelatedProduct?>? = null,
        var serves: Any? = null,
        var soldType: Int? = null,
        var subDescription: Any? = null,
        var type: String? = null,
        var updatedAt: String? = null,
        var weight: Any? = null
    ) {
        data class Recipies(
            var active: Int? = null,
            var categoryId: Int? = null,
            var createdAt: String? = null,
            var id: Int? = null,
            var ingredients: String? = null,
            var name: String? = null,
            var steps: String? = null,
            var thumbnailImage: String? = null,
            var updatedAt: String? = null,
            var videos: String? = null
        )

        data class RelatedProduct(
            var active: Int? = null,
            var arProductName: String? = null,
            var basketProductList: Any? = null,
            var cartId: Int? = 0,
            var cartonActive: Int? = null,
            var categoryId: Int? = null,
            var categoryName: String? = null,
            var createdAt: String? = null,
            var description: String? = null,
            var enProductName: String? = null,
            var id: Int? = null,
            var image: List<String?>? = null,
            var isBasket: Int? = null,
            var isFavorites: Int? = 0,
            var isStock: Int? = null,
            var mostWantedProduct: Int? = null,
            var name: String? = null,
            var newProduct: Int? = null,
            var noOfPieces: Int? = null,
            var offers: Int? = null,
            var parentId: Int? = null,
            var piecesActive: Int? = null,
            var price: Double? = 0.0,
            var normalPrice: Double? = 0.0,
            var offerPrice: Double? = 0.0,
            var productId: Int? = null,
            var quantity: Int? = 0,
            var recipiesId: Any? = null,
            var serves: Any? = null,
            var soldType: Int? = 0,
            var subDescription: Any? = null,
            var type: String? = null,
            var updatedAt: String? = null,
            var weight: String? = null

        )
    }
}