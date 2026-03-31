package com.app.delmon.Model

data class SpinnerListData(
    var `data`: List<Data?>? = null,
    var success: Boolean? = null
) {
    data class Data(
        var active: Int? = null,
        var created_at: String? = null,
        var id: Int? = null,
        var title: String? = null,
        var type: String? = null,
        var updated_at: String? = null
    )
}