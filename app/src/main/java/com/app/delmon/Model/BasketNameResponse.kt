package com.app.delmon.Model

data class BasketNameResponse(
    var `data`: List<Data?>? = null,
    var error: Boolean? = null,
    var message: String? = null
) {
    data class Data(
        var createdAt: String? = null,
        var id: Int? = null,
        var name: String? = null,
        var updatedAt: String? = null,
        var userId: Int? = null
    )
}