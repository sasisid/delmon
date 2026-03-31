package com.app.delmon.Model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.io.Serializable

class AutoCompleteAddressResponseModel : Serializable {

    @SerializedName("predictions")
    @Expose
    var predictions: List<PredictionAutoCompleted>? = null
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("error")
    @Expose
    var error: Boolean? = null
    @SerializedName("errorMessage")
    @Expose
    var errorMessage: String? = null


}

class PredictionAutoCompleted : Serializable {

    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("place_id")
    @Expose
    var placeId: String? = null
    @SerializedName("reference")
    @Expose
    var reference: String? = null

    @SerializedName("types")
    @Expose
    var types: List<String>? = null


}
