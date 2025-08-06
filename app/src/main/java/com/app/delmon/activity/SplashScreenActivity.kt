package com.app.delmon.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowCompat
import com.app.delmon.R
import com.app.delmon.Session.SharedHelper
import com.app.delmon.utils.BaseUtils
import com.app.delmon.utils.Constants
import com.app.delmon.utils.LanguageManager

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var sharedHelper: SharedHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        WindowCompat.setDecorFitsSystemWindows(window, false)
        sharedHelper= SharedHelper(this)
        LanguageManager.changeLanguage(this,sharedHelper.language)
        Constants.User.isLoggedIn = sharedHelper.loggedIn
         Constants.User.token = sharedHelper.token
         Constants.User.usertype = sharedHelper.userType

        Handler(Looper.getMainLooper()).postDelayed({
            if( sharedHelper.loggedIn) {
                val intent = Intent(this, SelectionActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, OnBoardingActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()
            }

        }, 300)

    }
}