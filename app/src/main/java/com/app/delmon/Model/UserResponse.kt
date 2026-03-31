package com.app.delmon.Model

data class UserResponse(
    var `data`: Data? = null,
    var error: Boolean? = null,
    var message: String? = null,

    ) {
    data class Data(
        var active: Int? = null,
        var countryCode: String? = null,
        var crNumber: String? = null,
        var createdAt: String? = null,
        var deviceToken: String? = "",
        var token: String? = "",
        var email: String? = "",
        var employeeNumber: String? = "",
        var firstName: Any? = null,
        var id: Int? = null,
        var isApprove: Int? = null,
        var isNewUser: Int? = null,
        var isNotification: Int? = null,
        var lastName: Any? = null,
        var loyaltyPoint: Double = 0.0,
        var merchantType: Any? = null,
        var mobileNumber: String? = null,
        var updatedAt: String? = null,
        var userName: String? = "",
        var userType: String? = null,
        var walletAmount: Double = 0.0
    )
}