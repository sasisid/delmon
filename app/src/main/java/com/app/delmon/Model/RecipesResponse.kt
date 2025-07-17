package com.app.delmon.Model

data class RecipesResponse(
    var `data`: List<Data?>? = null,
    var error: Boolean? = null,
    var message: String? = null
) {
    data class Data(
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
}