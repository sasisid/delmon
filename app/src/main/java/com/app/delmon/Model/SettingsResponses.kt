package com.app.delmon.Model

data class `SettingsResponses`(
    var `data`: Data? = null,
    var success: Boolean? = null
) {
    data class Data(
        var about: String? = null,
        var call: String = "",
        var countryCode: String? = null,
        var email: String = "",
        var faq: String? = null,
        var privacyPolicy: String? = null,
        var privacyPolicy_ar: String? = null,
        var support: String? = null,
        var termsAndCondition: String? = null,
        var termsAndCondition_ar: String? = null,
        var whatsAppNumber: String = ""
    ){
        constructor() : this(email="",call="",whatsAppNumber="")
    }
}
