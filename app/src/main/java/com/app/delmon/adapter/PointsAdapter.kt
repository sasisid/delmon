package com.app.delmon.adapter

import com.app.delmon.databinding.PointsCardBinding

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.delmon.Model.WalletResponse
import com.app.delmon.R
import com.app.delmon.interfaces.OnClickListener
import com.app.delmon.utils.BaseUtils
import com.bumptech.glide.Glide


class PointsAdapter(
    var context: Context,
    var data: List<WalletResponse.Data?>?,
    var onClickListener: OnClickListener,
    var from:String
    ): RecyclerView.Adapter<PointsAdapter.CategoryParentViewHolder>(){


    inner class CategoryParentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: PointsCardBinding = PointsCardBinding.bind(view)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryParentViewHolder {
        return CategoryParentViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.points_card,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryParentViewHolder, position: Int) {
        var weights =  500 * (1+position)
        var amt =  45 * (1+position)
        holder.binding.apply {
//            weight.text =   "$weights Kg / $amt BD"
            if (data!![position]!!.type =="SUB" || data!![position]!!.loyaltyType =="DEBIT" ){
                Glide.with(context).load(R.drawable.debit_arrow).placeholder(R.drawable.debit_arrow)
                    .into(holder.binding.arrowImg);
                orderId.text = data!![position]!!.orderId.toString() +"| Debit"

            }else{
                orderId.text = data!![position]!!.orderId.toString() +"| Credit"
            }
            if(from=="loyalty"){
                pointsCredit.text = data!![position]!!.usedLoyaltyPoint.toString()

            }else{
                pointsCredit.text = data!![position]!!.amount.toString() +" BD"

            }
            creditDate.text = BaseUtils.getFormatedDateUtc(data!![position]!!.createdAt!!,"yyyy-MM-dd'T'HH:mm:ss.SSSZ","EEEE, MMM d, yyyy")


        }

        holder.binding.root.setOnClickListener(){
            onClickListener.onClickItem(position)
            notifyDataSetChanged()

        }



    }

    override fun getItemCount(): Int {
        return data!!.size
    }


}