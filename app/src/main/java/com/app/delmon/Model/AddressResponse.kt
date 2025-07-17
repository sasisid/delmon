package com.app.delmon.Model

data class AddressResponse(
    var `data`: List<Data?>? = null,
    var error: Boolean? = null,
    var message: String? = null
) {
    data class Data(
        var active: Int? = null,
        var address: String? = "",
        var addressName: String? = null,
        var buildingName: String? = null,
        var createdAt: String? = null,
        var flatNo: String? = null,
        var flat: String? = null,
        var id: Int? = null,
        var landmark: String? = null,
        var latitude: String? = null,
        var longitude: String? = null,
        var saveAs: String? = null,
        var area: String? = null,
        var deliveryCharge: Double = 0.0,
        var blockNo: String? = null,
        var notes: String? = null,
        var houseNo: String? = null,
        var roadNo: String? = null,
        var updatedAt: String? = null,
        var userId: Int? = null,
        var pin: Int? = null,
        var zoneId: Int? = null,
        var zoneName: String? = null
    )
}