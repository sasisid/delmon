package com.app.delmon.Model

data class PinCodeResponse(
    var `data`: List<Data?>? = null,
    var error: Boolean? = null
) {
    data class Data(
        var active: Int? = null,
        var areaId: Int? = null,
        var createdAt: String? = null,
        var id: Int? = null,
        var pin: Int? = null,
        var updatedAt: String? = null
    )
}