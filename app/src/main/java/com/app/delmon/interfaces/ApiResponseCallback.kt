package com.app.delmon.interfaces

import org.json.JSONObject

interface ApiResponseCallback {
    fun setResponseSuccess(jsonObject: JSONObject)
    fun setErrorResponse(error : String)
}