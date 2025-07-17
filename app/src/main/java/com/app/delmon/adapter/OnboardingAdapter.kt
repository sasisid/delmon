package com.app.delmon.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.app.delmon.R
import com.app.delmon.fragment.onboarding.OnboardingFragment1

class OnboardingAdapter(manager: FragmentManager,
                        private val context : Context
) :FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    // Returns total number of pages
    override fun getCount(): Int {
        return NUM_ITEMS
    }

    // Returns the fragment to display for that page
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> OnboardingFragment1.newInstance(
                "Order from a wide selection of high-quality Chicken and Bird Feed.",
                "Shop with confidence knowing our products are high-quality and safe for your chickens and its feeding.",
                R.raw.first
            )
            1 -> OnboardingFragment1.newInstance(
                "Enjoy doorstep delivery for added convenience",
                "Our door delivery service provides added convenience by delivering the products directly to your doorstep, or you can choose to collect at a location convenient for you.",
                R.raw.second
            )
            2 -> OnboardingFragment1.newInstance(
                "Enjoy Merchant benefits for bulk orders.",
                "Our Chicken and Bird Feeding app makes it easy for merchants to order their feed and supplies right from their mobile devices",
                R.raw.third
            )
            else -> null
        }!!
    }

    // Returns the page title for the top indicator
    override fun getPageTitle(position: Int): CharSequence? {
        return "Page $position"
    }

    companion object {
        private const val NUM_ITEMS = 3
    }

}