package com.app.delmon.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.view.View
import java.util.*

object LanguageManager {

    fun changeLanguage(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)

        // Set layout direction based on the language
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale)
        }

        context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
    }

    fun setLayoutDirection(view: View, languageCode: String) {
        val layoutDirection = if (isRTL(languageCode)) View.LAYOUT_DIRECTION_RTL else View.LAYOUT_DIRECTION_LTR
        view.layoutDirection = layoutDirection
    }

    private fun isRTL(languageCode: String): Boolean {
        // Check if the language is a right-to-left language
        return languageCode.equals("ar", ignoreCase = true) || languageCode.equals("fa", ignoreCase = true)
    }
}
