package com.app.delmon.Model

data class TrendingProductResponse(
    val `data`: List<Data>? = null,
    var error: Boolean? = null,
    var message: String? = null
) {
    data class Data(
        val arProductName: String? = null,
        val enProductName: String? = null,
        val name: String? = null,
        val productId: Int? = null,
        val quantity: String? = null,
    )
}