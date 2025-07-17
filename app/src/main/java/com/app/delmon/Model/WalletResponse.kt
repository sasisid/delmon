package com.app.delmon.Model

data class WalletResponse(
    var `data`: List<Data?>? = null,
    var error: Boolean? = null
) {
    data class Data(
        var amount: Double? = 0.0,
        var createdAt: String? = null,
        var id: Int? = null,
        var orderId: Int? = 0,
        var paymentId: Any? = null,
        var paymentType: String? = null,
        var productType: Any? = null,
        var type: String? = "",
        var usedLoyaltyPoint: Double? = 0.0,
        var loyaltyType: String? = "",
        var updatedAt: String? = null,
        var userId: Int? = null
    )
}