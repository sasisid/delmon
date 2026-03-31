package com.app.delmon.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.delmon.Model.ProductResponse
import com.app.delmon.R
import com.app.delmon.databinding.BasketProductCardBinding
import com.app.delmon.databinding.CategoryProductCardBinding
import com.app.delmon.interfaces.OnClickListener
import com.app.delmon.interfaces.OnClickListnereWithType

class BasketProductAdapter(
    var context: Context,
     var prodctresponse: ArrayList<ProductResponse.Data>,
    var onClickListener: OnClickListnereWithType,
    var onRootClickListener: OnClickListener,
    var from: String,
): RecyclerView.Adapter<BasketProductAdapter.BasketParentViewHolder>() {
    var data = prodctresponse
    inner class BasketParentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: BasketProductCardBinding = BasketProductCardBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketParentViewHolder {
        return BasketParentViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.basket_product_card,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: BasketParentViewHolder, position: Int) {
        holder.binding.apply {
            title.text = data[position].name +"(${data[position].weight})"
            if (data[position].name.isNullOrEmpty()){
                title.text = data[position].enProductName +"(${data[position].weight})"
            }
            if (from=="user") {
                price.text = data[position].price.toString() + " BD"
                close.setOnClickListener {
                    onClickListener.onClickItem(position, "remove")
                }
            }else{
                price.visibility = View.GONE
                close.visibility = View.GONE
            }
        }

    }


}