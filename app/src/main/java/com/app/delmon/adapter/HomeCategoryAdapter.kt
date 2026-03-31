package com.app.delmon.adapter


import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.delmon.Model.HomeResponse
import com.app.delmon.R
import com.app.delmon.databinding.HomeCategoryCardBinding
import com.app.delmon.interfaces.OnClickListener
import com.app.delmon.utils.Constants
import com.bumptech.glide.Glide


class HomeCategoryAdapter(
    var context: Context,
    var data: List<HomeResponse.Data.Category?>?,
    var onClickListener: OnClickListener
): RecyclerView.Adapter<HomeCategoryAdapter.MyBookingViewHolder>() {

    var colors = arrayOf<String>("#FEC939", "#F29428", "#85BDEA", "#E86C31","#A889F4","#E31D1A","#FEC939", "#F29428", "#85BDEA", "#E86C31","#A889F4","#E31D1A")
    var myImageList = intArrayOf(R.drawable.poultryfeed, R.drawable.birdsfeeding, R.drawable.ruminafeed, R.drawable.rawfeed, R.drawable.other_feeding,)

    inner class MyBookingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: HomeCategoryCardBinding = HomeCategoryCardBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyBookingViewHolder {
        if (Constants.User.apptype==2){
             colors = arrayOf<String>("#B4E4CB", "#CEDEFF", "#FFCEAA", "#EDC6FF","#7498E3AC")
        }
        return MyBookingViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.home_category_card,
                parent,
                false
            )
        )
    }



    override fun onBindViewHolder(holder: MyBookingViewHolder, position: Int) {
        Log.d("TAG", "onBindViewHolder: ${data!![position]!!}")
        holder.binding.apply {
            if (data!![position]!!.colorCode!="" && data!![position]!!.colorCode!!.length >= 7) {
                catLay.setBackgroundColor(Color.parseColor(data!![position]!!.colorCode))
            }
            catNum.text = "0${position+1}"
            catTitle.text = data!![position]!!.name
            root.setOnClickListener {
                onClickListener.onClickItem(position)
            }
//            if (MainActivity.apptype==2){
                Glide.with(context).load(data!![position]!!.image).into(holder.binding.catImg);
//            }
            }


        }

    override fun getItemCount(): Int {

//        if (MainActivity.apptype==2){
//            return 5
//        }
       return data!!.size
    }



}