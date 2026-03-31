package com.app.delmon.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.delmon.Model.BasketNameResponse
import com.app.delmon.R
import com.app.delmon.databinding.BasketNameListLayoutBinding

import com.app.delmon.interfaces.OnClickListnereWithType

class BasketNameListAdapter (
    var context: Context,
    var from: String,
    var data: ArrayList<BasketNameResponse.Data?>,
    var onClickListener: OnClickListnereWithType,
    var positiontofix: Int,
    ): RecyclerView.Adapter<BasketNameListAdapter.BasketParentViewHolder>(){
    var row_index: Int = -1


    inner class BasketParentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: BasketNameListLayoutBinding = BasketNameListLayoutBinding.bind(view)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketParentViewHolder {
        return BasketParentViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.basket_name_list_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BasketParentViewHolder, position: Int) {
        row_index = positiontofix
        holder.binding.basketName.text = data[position]!!.name
        holder.binding.addToCart.visibility = View.GONE
        holder.binding.root.setOnClickListener(){
            positiontofix = position
            row_index = positiontofix
            onClickListener.onClickItem(position,"")
            notifyDataSetChanged()

        }
        if (from=="details"){
            holder.binding.remove.visibility = View.GONE
            holder.binding.addToCart.visibility = View.GONE
        }
        holder.binding.remove.setOnClickListener {
            onClickListener.onClickItem(position,"remove")
        }
        holder.binding.addToCart.setOnClickListener {
            onClickListener.onClickItem(position,"moveCart")
        }
        if(row_index==position){
            //  taskFragment._binding!!.categoryRecyc.smoothScrollToPosition(position)
            holder.binding.basketName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_awesome_check_circle__selected, 0);

        }
        else
        {
            holder.binding.basketName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_awesome_check_circle_unselected, 0);

        }


    }

    override fun getItemCount(): Int {
        return data.size
    }


}