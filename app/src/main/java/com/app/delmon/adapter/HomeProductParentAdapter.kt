package com.app.delmon.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.delmon.Model.HomeResponse
import com.app.delmon.R
import com.app.delmon.databinding.HomeProductParentBinding
import com.app.delmon.interfaces.OnClickListener
import com.app.delmon.interfaces.OnClickListnereWithType
import com.app.delmon.interfaces.OnclickTwoPositionListner

class HomeProductParentAdapter(var context: Context,
                               var data: List<HomeResponse.Data.Productdata?>,
                               var onclickTwoPositionListner: OnclickTwoPositionListner,
                               ): RecyclerView.Adapter<HomeProductParentAdapter.HomeParentViewHolder>() {


    inner class HomeParentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: HomeProductParentBinding = HomeProductParentBinding.bind(view)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): HomeParentViewHolder {
        return HomeParentViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.home_product_parent,
                parent,
                false
            )
        )    }

    override fun onBindViewHolder(
        holder: HomeProductParentAdapter.HomeParentViewHolder,
        @SuppressLint("RecyclerView") position: Int,
    ) {
        holder.binding.title.text = data[position]!!.title
        holder.binding.productchildrecyc.layoutManager = GridLayoutManager(context,1,
            GridLayoutManager.HORIZONTAL,false)
        holder.binding.productchildrecyc.scheduleLayoutAnimation();
        holder.binding.productchildrecyc.adapter =
            HomeProductChildAdapter(context,
                data[position]!!.data,object :
                    OnClickListnereWithType {
                    override fun onClickItem(subposition: Int,type: String) {
                        onclickTwoPositionListner.onClickItem(position,subposition)
                    }
                }
            )

    }

    override fun getItemCount(): Int {
        return data.size
    }
}