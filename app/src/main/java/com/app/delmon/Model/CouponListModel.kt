package com.app.delmon.Model


data class CouponListModel(
    var `data`: List<Data>? = null,
    var success: Boolean? = null
) {
    data class Data(
        var id: Int? = null,
        var couponName: String? = null,
        var couponCode: String? = null,
        var discountPercentage: Int? = null,
        var maximumDiscount: Int? = null,
        var active: Int? = null,
        var createdAt: String? = null,
        var updatedAt: String? = null,
        var title: String? = null,
        var description: String? = null,
        var enCouponName: String? = null,
        var arCouponName: String? = null,
        var enDescription: String? = null,
        var arDescription: String? = null
    )
}