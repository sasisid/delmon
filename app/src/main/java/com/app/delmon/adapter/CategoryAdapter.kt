package com.app.delmon.adapter


import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.delmon.Model.CategoryResponse
import com.app.delmon.R
import com.app.delmon.databinding.CategoryCardBinding
import com.app.delmon.interfaces.OnClickListener


class CategoryAdapter(
    var context: Context,
    var data: List<CategoryResponse.Data?>,
    var onClickListener: OnClickListener,
    var positiontofix: Int,


    ): RecyclerView.Adapter<CategoryAdapter.CategoryParentViewHolder>(){
    var row_index: Int = -1


    inner class CategoryParentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: CategoryCardBinding = CategoryCardBinding.bind(view)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryParentViewHolder {
        return CategoryParentViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.category_card,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryParentViewHolder, position: Int) {
        row_index = positiontofix
        holder.binding.catName.text = data[position]!!.name
        holder.binding.root.setOnClickListener(){
            positiontofix = position
            row_index = positiontofix
            onClickListener.onClickItem(position)
            notifyDataSetChanged()

        }
        if(row_index==position){
            //  taskFragment._binding!!.categoryRecyc.smoothScrollToPosition(position)
            holder.binding.catName.setTextColor(Color.parseColor("#E31D1A"));
            holder.binding.catName.setBackgroundResource(R.drawable.rectangle_secondary_border_radius)
        }
        else
        {
            holder.binding.catName.setTextColor(Color.parseColor("#000000"));
            holder.binding.catName.setBackgroundResource(R.drawable.grey_rectangle)

        }


    }

    override fun getItemCount(): Int {
        return data.size
    }


}