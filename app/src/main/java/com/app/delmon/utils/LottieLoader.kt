package com.app.delmon.utils

import android.content.Context
import android.util.Log
import android.view.View
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieComposition


class LottieLoader(private val context: Context) {

    private val lottieView: LottieAnimationView = LottieAnimationView(context)

    init {
        lottieView.setAnimation("white_loader.json")
        lottieView.loop(true)
        lottieView.speed = 1.0f
    }

    fun loadAnimation() {
        Log.e("appSample", "loadAnimation")

        LottieComposition.Factory.fromAssetFileName(
            context,
            "white_loader.json"
        ) { composition ->
            composition?.let {
                Log.e("appSample", "let")
                lottieView.visibility = View.VISIBLE
                lottieView.setComposition(it)
                Log.e("appSample", "let_ends")
            }
        }
    }

    fun getLottieView(): LottieAnimationView {
        return lottieView
    }
}