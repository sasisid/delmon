package com.app.delmon.adapter


import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.delmon.Model.CategoryResponse
import com.app.delmon.R
import com.app.delmon.Session.SharedHelper
import com.app.delmon.databinding.CategoryCardBinding
import com.app.delmon.interfaces.OnClickListener

private lateinit var sharedHelper: SharedHelper

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
        sharedHelper= SharedHelper(context)

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
        if(sharedHelper!!.language == "ar") {
            holder.binding.catName.text = data[position]!!.arName
        }else{
            holder.binding.catName.text = data[position]!!.name
        }
        holder.binding.root.setOnClickListener(){
            positiontofix = position
            row_index = positiontofix
            onClickListener.onClickItem(position)
            notifyDataSetChanged()

        }
        val strokePx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            1.5f,
            context.resources.displayMetrics,
        ).toInt().coerceAtLeast(1)
        if (row_index == position) {
            holder.binding.tabCard.strokeWidth = strokePx
            holder.binding.tabCard.strokeColor = ContextCompat.getColor(context, R.color.secondary_color)
            holder.binding.tabCard.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))
            holder.binding.catName.setTextColor(ContextCompat.getColor(context, R.color.secondary_color))
        } else {
            holder.binding.tabCard.strokeWidth = 0
            holder.binding.tabCard.strokeColor = Color.TRANSPARENT
            holder.binding.tabCard.setCardBackgroundColor(ContextCompat.getColor(context, R.color.btn_grey))
            holder.binding.catName.setTextColor(ContextCompat.getColor(context, R.color.black))
        }


    }

    override fun getItemCount(): Int {
        return data.size
    }


}