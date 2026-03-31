package com.app.delmon.Model

data class SubCategoryResponse(
    var data: List<Data?>? = null,
    var error: Boolean? = null,
    var message: String? = null
) {
    data class Data(
        var active: Int? = null,
        var arName: String? = null,
        var colorCode: String? = null,
        var createdAt: String? = null,
        var enName: String? = null,
        var id: Int? = null,
        var image: String? = null,
        var name: String? = null,
        var parentId: Int? = null,
        var type: String? = null,
        var updatedAt: String? = null
    )
}