package com.app.delmon.Model

data class ZoneResponse(
    var `data`: List<Data?>? = null,
    var error: Boolean? = null
) {
    data class Data(
        var active: Int? = null,
        var arName: String? = null,
        var createdAt: String? = null,
        var deliveryCharge: Double? = null,
        var id: Int? = null,
        var name: String? = null,
        var updatedAt: String? = null
    )
}