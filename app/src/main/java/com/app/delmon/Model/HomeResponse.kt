package com.app.delmon.Model

data class HomeResponse(
    var `data`: Data? = null,
    var error: Boolean? = null,
    var message: String? = null,
    var popupAdvertisement: String? = null
) {
    data class Data(
        var banner: List<Banner?>? = null,
        var category: List<Category?>? = null,
        var productdata: List<Productdata?>? = null
    ) {
        data class Banner(
            var active: Int? = null,
            var arImage: String? = null,
            var createdAt: String? = null,
            var enImage: String? = null,
            var id: Int? = null,
            var image: String? = null,
            var name: String? = null,
            var type: String? = null,
            var updatedAt: String? = null
        )

        data class Category(
            var active: Int? = null,
            var arName: String? = null,
            var colorCode: String? = "#FA3678",
            var createdAt: String? = null,
            var enName: String? = null,
            var id: Int? = null,
            var image: String? = null,
            var name: String? = null,
            var parentId: Int? = null,
            var path: Any? = null,
            var type: String? = null,
            var updatedAt: String? = null
        )

        data class Productdata(
            var `data`: List<Data?>? = null,
            var title: String? = null
        ) {
            data class Data(
                var active: Int? = null,
                var arProductName: String? = null,
                var basketProductList: Any? = null,
                var cartonActive: Int? = null,
                var categoryId: Int? = null,
                var categoryName: String? = null,
                var createdAt: String? = null,
                var description: String? = null,
                var enProductName: String? = null,
                var id: Int? = null,
                var image: List<String?>? = null,
                var isBasket: Int? = null,
                var isFavorites: Int? = null,
                var isStock: Int? = null,
                var mostWantedProduct: Int? = null,
                var name: String? = null,
                var newProduct: Int? = null,
                var noOfPieces: Int? = null,
                var offers: Int? = null,
                var parentId: Int? = null,
                var piecesActive: Int? = null,
                var price: Int? = null,
                var recipiesId: List<Int?>? = null,
                var serves: String? = null,
                var soldType: Int? = null,
                var subDescription: String? = null,
                var type: String? = null,
                var updatedAt: String? = null,
                var weight: String? = null
            )
        }
    }
}