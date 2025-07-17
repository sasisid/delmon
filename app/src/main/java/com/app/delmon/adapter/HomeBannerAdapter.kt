package com.app.delmon.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.app.delmon.Model.HomeResponse
import com.app.delmon.R

import com.bumptech.glide.Glide

class HomeBannerAdapter(private val context: Context, var data: List<HomeResponse.Data.Banner?>?) : PagerAdapter() {
    override fun getCount(): Int {
        return data!!.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View =  (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
            R.layout.banner_card, null)
        val ivImages = view.findViewById<ImageView>(R.id.banner_img)

//        if (MainActivity.apptype == 2){
//            Glide.with(context)
//                .load(R.drawable.dummy_feeding_banner)
//                .into(ivImages);
//        }else{
//            Glide.with(context)
//                .load(R.drawable.dummy_banner)
//                .into(ivImages);
//        }
        if (!data!![position]!!.image.isNullOrEmpty())
            Glide.with(context).load(data!![position]!!.image).placeholder(R.drawable.placeholder_image).error(R.drawable.placeholder_image).into(ivImages)


        val vp = container as ViewPager
        vp.addView(view, 0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val vp = container as ViewPager
        val view = `object` as View
        vp.removeView(view)
    }
}