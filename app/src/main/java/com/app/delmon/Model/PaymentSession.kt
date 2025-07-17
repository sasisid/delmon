package com.app.delmon.Model

data class PaymentSession(
    var merchant: String? = null,
    var result: String? = null,
    var session: Session? = null
) {
    data class Session(
        var aes256Key: String? = null,
        var authenticationLimit: Int? = null,
        var id: String? = null,
        var updateStatus: String? = null,
        var version: String? = null
    )
}