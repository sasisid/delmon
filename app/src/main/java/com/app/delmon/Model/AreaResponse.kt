package com.app.delmon.Model

data class AreaResponse(
    var `data`: List<Data?>? = null,
    var error: Boolean? = null
) {
    data class Data(
        var id: Int? = null,
        var areaName: String? = null,
        var arAreaName: String? = null,
        var zoneId: Int? = null,
        var active: Int? = null,
        var createdAt: String? = null,
        var updatedAt: String? = null
    )
}