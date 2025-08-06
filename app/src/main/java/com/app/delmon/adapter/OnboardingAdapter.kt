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
                context.getString(R.string.onboarding_title_1),
                context.getString(R.string.onboarding_desc_1),
                R.raw.first
            )
            1 -> OnboardingFragment1.newInstance(
                context.getString(R.string.onboarding_title_2),
                context.getString(R.string.onboarding_desc_2),
                R.raw.second
            )
            2 -> OnboardingFragment1.newInstance(
                context.getString(R.string.onboarding_title_3),
                context.getString(R.string.onboarding_desc_3),
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