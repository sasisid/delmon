package com.app.delmon.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.app.delmon.Model.ProductResponse
import com.app.delmon.R
import com.app.delmon.Session.SharedHelper
import com.app.delmon.databinding.CategoryProductCardBinding
import com.app.delmon.interfaces.OnClickListener
import com.app.delmon.interfaces.OnClickListnereWithType
import com.app.delmon.utils.Constants
import com.bumptech.glide.Glide

private lateinit var sharedHelper: SharedHelper

class CategoryProductAdapter(
    var context: Context,
    var data: ArrayList<ProductResponse.Data>,
    var onClickListener: OnClickListnereWithType,
    var onRootClickListener: OnClickListener,
    var from: String,
    ): RecyclerView.Adapter<CategoryProductAdapter.CategoryParentViewHolder>(){


    inner class CategoryParentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: CategoryProductCardBinding = CategoryProductCardBinding.bind(view)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryParentViewHolder {
        sharedHelper= SharedHelper(context)

        return CategoryParentViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.category_product_card,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryParentViewHolder, position: Int) {

        holder.binding.root.setOnClickListener {
            onRootClickListener.onClickItem(position)
        }
        holder.binding.apply {

            /*if(data[position]!!.isStock!! > 0) {
                stock.text = context.resources.getString(R.string.instock)
                stock.setBackgroundColor(context.resources.getColor(R.color.secondary_feeding_color))
            } else {
                stock.text = context.resources.getString(R.string.out_of_stock)
                stock.setBackgroundColor(context.resources.getColor(R.color.delmon_red))
            }*/

            stock.visibility = View.GONE

            if (from=="search"||from=="fav") {
                add.visibility = View.GONE
                addBasket.visibility = View.GONE
                productPrice.visibility = View.GONE
                productWeight.visibility = View.GONE
                otherLayout.visibility = View.GONE
            } else if (from=="basket" ) {
                add.visibility = View.GONE
                addBasket.visibility = View.GONE
                productPrice.visibility = View.GONE
                productWeight.visibility = View.GONE
                otherLayout.visibility = View.GONE
                fav.visibility = View.GONE
                remove.visibility = View.VISIBLE
            }

            remove.setOnClickListener {
                onClickListener.onClickItem(position,"remove")
            }
            if(sharedHelper!!.language == "en") {
                productName.text = data[position]!!.name
                productDesc.text = data[position]!!.description

            }else{
                productName.text = data[position]!!.arProductName
                productDesc.text = data[position]!!.arDescription
            }
            productWeight.text = data[position]!!.weight
            productSize1.text = data[position]!!.weight
            productPrice.text = data[position]!!.price.toString() + " BD"
            productPrice1.text = data[position]!!.price.toString() + " BD"
            if (Constants.User.apptype==2){
                addBasket.visibility = View.GONE
            }
            if (!data[position].image.isNullOrEmpty()) {
                Glide.with(context).load(data[position].image!![0])
                    .placeholder(R.drawable.placeholder_image).error(R.drawable.placeholder_image)
                    .into(imageView3)
            }else{
                Glide.with(context).load(R.drawable.placeholder_image)
                    .placeholder(R.drawable.placeholder_image).error(R.drawable.placeholder_image)
                    .into(imageView3)
            }
            add.setOnClickListener {
                onClickListener.onClickItem(position,"add")
            }
            addBasket.setOnClickListener {
                onClickListener.onClickItem(position,"basket")
            }
            fav.setOnClickListener {
                onClickListener.onClickItem(position,"fav")
            }
        }

        holder.binding.productWeight.setOnClickListener(){

            onClickListener.onClickItem(position,"weight")
            notifyDataSetChanged()

        }




    }

    override fun getItemCount(): Int {
        return data.size
    }


}