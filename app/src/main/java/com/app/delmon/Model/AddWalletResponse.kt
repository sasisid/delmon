package com.app.delmon.Model

data class AddWalletResponse(
    var `data`: Data? = null,
    var error: Boolean? = null,
    var message: String? = null,
    var walletAmount: Int? = null
) {
    data class Data(
        var amount: Int? = null,
        var createdAt: String? = null,
        var id: Int? = null,
        var orderId: Int? = null,
        var paymentType: String? = null,
        var type: String? = null,
        var updatedAt: String? = null,
        var userId: String? = null
    )
}