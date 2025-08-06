package com.app.delmon.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.app.delmon.Model.HomeResponse
import com.app.delmon.R
import com.app.delmon.databinding.CategoryProductCardBinding
import com.app.delmon.databinding.HomeProductChildBinding
import com.app.delmon.databinding.HomeProductParentBinding
import com.app.delmon.interfaces.OnClickListener
import com.app.delmon.interfaces.OnClickListnereWithType
import com.bumptech.glide.Glide

class HomeProductChildAdapter(
    var context: Context,
    var data: List<HomeResponse.Data.Productdata.Data?>?,
    var onClickListener: OnClickListnereWithType
): RecyclerView.Adapter<HomeProductChildAdapter.HomeChildViewHolder>() {

    inner class HomeChildViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: HomeProductChildBinding = HomeProductChildBinding.bind(view)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): HomeProductChildAdapter.HomeChildViewHolder {
        return HomeChildViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.home_product_child,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: HomeProductChildAdapter.HomeChildViewHolder,
        position: Int,
    ) {
        holder.binding.root.setOnClickListener {
//            onRootClickListener.onClickItem(position)
            onClickListener.onClickItem(position,"home")

        }
        holder.binding.apply {
            productName.text = data!![position]!!.name
            productDesc.text = data!![position]!!.description
            productWeight.text = data!![position]!!.weight
            productSize1.text = data!![position]!!.weight
            productPrice.text = "BD "+ data!![position]!!.price.toString()
            productPrice1.text = "BD "+ data!![position]!!.price.toString()
            try {
                if (!data!![position]?.image .isNullOrEmpty()) {
                    Glide.with(context).load(data!![position]!!.image!![0])
                        .placeholder(R.drawable.placeholder_image).error(R.drawable.placeholder_image)
                        .into(imageView3)
                }
            }catch (e: Exception){
                e.printStackTrace()
            }

        }
    }

    override fun getItemCount(): Int {
        return data!!.size
    }
}