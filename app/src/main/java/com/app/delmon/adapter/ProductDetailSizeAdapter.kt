package com.app.delmon.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.delmon.Model.ProductDetailResponse
import com.app.delmon.R
import com.app.delmon.databinding.ProductSizeCardBinding
import com.app.delmon.interfaces.OnClickListnereWithType

class ProductDetailSizeAdapter(
    var context: Context,
    var data: List<ProductDetailResponse.Data.RelatedProduct?>?,
    var productId:Int,
    var positiontofix: Int,
    var onClickListener: OnClickListnereWithType,

    ): RecyclerView.Adapter<ProductDetailSizeAdapter.CategoryParentViewHolder>() {
    var row_index: Int = -1

    inner class CategoryParentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: ProductSizeCardBinding = ProductSizeCardBinding.bind(view)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryParentViewHolder {
        return CategoryParentViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.product_size_card,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryParentViewHolder, position: Int) {
        row_index = positiontofix
        holder.binding.root.setOnClickListener {
            onClickListener.onClickItem(position,"")
            positiontofix = position
            row_index = positiontofix
            notifyDataSetChanged()
        }

        holder.binding.apply {
        size.text = data!![position]!!.weight.toString()
        }
        if(row_index==position){
            //  taskFragment._binding!!.categoryRecyc.smoothScrollToPosition(position)
            holder.binding.size.setBackgroundResource(R.drawable.rectangle_secondary_border_radius)
        }
        else{
            holder.binding.size.setBackgroundResource(R.drawable.rectangle_grey_border)
        }


    }

    override fun getItemCount(): Int {
        return data!!.size
    }




}