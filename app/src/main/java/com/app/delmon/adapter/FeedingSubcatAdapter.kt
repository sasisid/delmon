package com.app.delmon.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.delmon.Model.SubCategoryResponse
import com.app.delmon.R
import com.app.delmon.databinding.FeedSubcatCardBinding
import com.app.delmon.interfaces.OnClickListener


class FeedingSubcatAdapter(
    var context: Context,
    var data: List<SubCategoryResponse.Data?>,
    var onClickListener: OnClickListener,

    ): RecyclerView.Adapter<FeedingSubcatAdapter.CategoryParentViewHolder>(){


    inner class CategoryParentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: FeedSubcatCardBinding = FeedSubcatCardBinding.bind(view)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryParentViewHolder {
        return CategoryParentViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.feed_subcat_card,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryParentViewHolder, position: Int) {
        var weights =  500 * (1+position)
        var amt =  45 * (1+position)
        holder.binding.apply {
            subcatName.text = data[position]!!.name
        }

        holder.binding.root.setOnClickListener(){
            onClickListener.onClickItem(position)
            notifyDataSetChanged()

        }



    }

    override fun getItemCount(): Int {
        return data.size
    }


}