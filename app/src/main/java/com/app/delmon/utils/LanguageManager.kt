package com.app.delmon.utils

import android.annotation.TargetApi
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import android.util.Log
import android.view.View
import com.app.delmon.Session.SharedHelper
import java.util.*

object LanguageManager {

    fun onAttach(context: Context): Context {
        val lang = getPersistedData(context, Locale.getDefault().language)
        return setLocale(context, lang)
    }

    fun onAttach(context: Context, defaultLanguage: String): Context {
        val lang = getPersistedData(context, defaultLanguage)
        return setLocale(context, lang)
    }

    private fun getPersistedData(context: Context, defaultLanguage: String): String {
        val sharedHelper = SharedHelper(context)
        return if (sharedHelper.language.isEmpty()) defaultLanguage else sharedHelper.language
    }

    fun setLocale(context: Context, languageCode: String): Context {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context, languageCode)
        } else {
            updateResourcesLegacy(context, languageCode)
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)

        return context.createConfigurationContext(configuration)
    }

    @Suppress("DEPRECATION")
    private fun updateResourcesLegacy(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val resources = context.resources
        val configuration = resources.configuration
        configuration.locale = locale
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale)
        }
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }

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
