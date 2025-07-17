package com.app.delmon.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.delmon.Model.ProductDetailResponse
import com.app.delmon.R
import com.app.delmon.databinding.RecipesCardBinding
import com.app.delmon.interfaces.OnClickListener
import com.squareup.picasso.Picasso

class DetailsPageRecipiesAdapter(
    var context: Context,
    var data: List<ProductDetailResponse.Data.Recipies?>?,
    var from: String,
    var onClickListener: OnClickListener,
): RecyclerView.Adapter<DetailsPageRecipiesAdapter.CategoryParentViewHolder>() {


    inner class CategoryParentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: RecipesCardBinding = RecipesCardBinding.bind(view)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryParentViewHolder {
        return CategoryParentViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.recipes_card,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryParentViewHolder, position: Int) {


        holder.binding.apply {
            recipesName.text = data!![position]!!.name
            title.text = data!![position]!!.name
            Picasso.get().load(data!![position]!!.thumbnailImage).placeholder(R.drawable.placeholder_image).error(
                R.drawable.placeholder_image).into(thumbnailImage)
                if (from=="first"){
                mainLayout.visibility = View.VISIBLE
                secondLayout.visibility = View.GONE
            }else if (from=="second"){
                mainLayout.visibility = View.GONE
                secondLayout.visibility = View.VISIBLE
            }
            if (!data!![position]!!.thumbnailImage.isNullOrEmpty())
                Picasso.get().load(data!![position]!!.thumbnailImage).placeholder(R.drawable.placeholder_image).error(R.drawable.placeholder_image).into(recipesImg)

        }
        holder.binding.root.setOnClickListener {
            onClickListener.onClickItem(position)
        }


    }

    override fun getItemCount(): Int {
        return data!!.size
    }
}