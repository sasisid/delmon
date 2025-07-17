package com.app.delmon.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.delmon.Model.AdminBasketResponse
import com.app.delmon.Model.BasketNameResponse
import com.app.delmon.R
import com.app.delmon.databinding.BasketNameListLayoutBinding
import com.app.delmon.databinding.HomeProductChildBinding

import com.app.delmon.interfaces.OnClickListnereWithType
import com.bumptech.glide.Glide

class AdminBasketListAdapter (
    var context: Context,
    var from: String,
    var data: ArrayList<AdminBasketResponse.Data?>,
    var onClickListener: OnClickListnereWithType,
    var positiontofix: Int,
): RecyclerView.Adapter<AdminBasketListAdapter.BasketParentViewHolder>(){
    private var row_index: Int = -1


    inner class BasketParentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: HomeProductChildBinding = HomeProductChildBinding.bind(view)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketParentViewHolder {
        return BasketParentViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.home_product_child,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BasketParentViewHolder, position: Int) {
        row_index = positiontofix
//        holder.binding.basketName.text = data[position]!!.name
//        holder.binding.addToCart.visibility = View.GONE

        holder.binding.apply {
            productName.text = data!![position]!!.name.toString()
            productDesc.text = data!![position]!!.description
            productWeight.text = data!![position]!!.weight.toString()
            data!![position]!!.weight.also { productSize1.text = it.toString() }
            productPrice.text = "BD "+ data!![position]!!.price.toString()
            productPrice1.text = "BD "+ data!![position]!!.price.toString()
            Glide.with(context).load(data!![position]!!.image!![0]).placeholder(R.drawable.placeholder_image).error(R.drawable.placeholder_image).into(imageView3)

        }

        holder.binding.root.setOnClickListener(){
            positiontofix = position
            row_index = positiontofix
            onClickListener.onClickItem(position,"")
            notifyDataSetChanged()

        }
//        if (from=="details"){
//            holder.binding.remove.visibility = View.GONE
//            holder.binding.addToCart.visibility = View.GONE
//        }
        holder.binding.remove.setOnClickListener {
            onClickListener.onClickItem(position,"remove")
        }
//        holder.binding.addToCart.setOnClickListener {
//            onClickListener.onClickItem(position,"moveCart")
//        }
        if(row_index==position){
            //  taskFragment._binding!!.categoryRecyc.smoothScrollToPosition(position)
//            holder.binding.basketName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_awesome_check_circle__selected, 0);

        }
        else
        {
//            holder.binding.basketName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_awesome_check_circle_unselected, 0);
//
        }


    }

    override fun getItemCount(): Int {
        return data.size
    }


}