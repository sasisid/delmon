package com.app.delmon.Model

data class CheckUserResponse(
    var `data`: Data? = null,
    var error: Boolean? = null,
    var message: String? = null,
    var otp: Int? = null

) {
    data class Data(
        var isRegistered: Boolean? = false,
        var otp: Int? = null,
        var isApprove: Int? = null
    )
}