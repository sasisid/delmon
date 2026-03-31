package com.app.delmon.Model

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

/** Parses API booleans that may be sent as JSON boolean or as 0/1. */
internal object FlexibleBoolTypeAdapter : JsonDeserializer<Boolean?> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?,
    ): Boolean? {
        if (json == null || json.isJsonNull) return null
        val p = json.asJsonPrimitive
        return when {
            p.isBoolean -> p.asBoolean
            p.isNumber -> p.asInt != 0
            p.isString -> p.asString.equals("true", ignoreCase = true) || p.asString == "1"
            else -> null
        }
    }
}

/** Parses amounts sent as JSON number or string (e.g. `"0.00"`). */
internal object FlexibleDoubleTypeAdapter : JsonDeserializer<Double> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?,
    ): Double {
        if (json == null || json.isJsonNull) return 0.0
        val p = json.asJsonPrimitive
        return when {
            p.isNumber -> p.asDouble
            p.isString -> p.asString.toDoubleOrNull() ?: 0.0
            else -> 0.0
        }
    }
}
