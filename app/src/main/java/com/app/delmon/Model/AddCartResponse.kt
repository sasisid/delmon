package com.app.delmon.Model

data class AddCartResponse(
    var data: Data? = null,
    var error: Boolean? = null,
    var totalCartCount: Int? = 0,

    var massage: String? = null
) {
    data class Data(
        var createdAt: String? = null,
        var id: Int? = null,
        var image: String? = null,
        var productId: Int? = null,
        var quantity: Int? = null,
        var type: String? = null,
        var updatedAt: String? = null,
        var userId: Int? = null
    )
}