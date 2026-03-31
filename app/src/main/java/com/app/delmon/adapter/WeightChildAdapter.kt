package com.app.delmon.adapter


import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.delmon.R
import com.app.delmon.activity.MainActivity
import com.app.delmon.databinding.CategoryCardBinding
import com.app.delmon.databinding.ChooseWeightChildCardBinding
import com.app.delmon.interfaces.OnClickListener
import com.app.delmon.utils.Constants


class WeightChildAdapter(
    var context: Context,
    var onClickListener: OnClickListener,

    ): RecyclerView.Adapter<WeightChildAdapter.CategoryParentViewHolder>(){


    inner class CategoryParentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: ChooseWeightChildCardBinding = ChooseWeightChildCardBinding.bind(view)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryParentViewHolder {
        return CategoryParentViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.choose_weight_child_card,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryParentViewHolder, position: Int) {
        var weights =  500 * (1+position)
        var amt =  45 * (1+position)
        holder.binding.apply {
            weight.text =   "$weights Kg / $amt BD"
            if (Constants.User.apptype==2){
                addBasket.visibility = View.GONE
            }
        }


        holder.binding.root.setOnClickListener(){
            onClickListener.onClickItem(position)
            notifyDataSetChanged()

        }



    }

    override fun getItemCount(): Int {
        return 3
    }


}