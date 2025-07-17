package com.app.delmon.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.delmon.Model.CouponListModel
import com.app.delmon.R
import com.app.delmon.Session.SharedHelper
import com.app.delmon.databinding.CardCouponListBinding
import com.app.delmon.interfaces.OnClickListener

class CouponListAdapter(var context: Context, var list: List<CouponListModel.Data>, var onClickListener: OnClickListener): RecyclerView.Adapter<CouponListAdapter.ViewHolder>(){
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: CardCouponListBinding = CardCouponListBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
            R.layout.card_coupon_list,
            parent,
            false
        )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.code.text = list[position].title
        if(SharedHelper(context).language == "en"){
            holder.binding.name.text = list[position].enCouponName
            holder.binding.desc.text = list[position].enDescription
        }
        else {
            holder.binding.name.text = list[position].arCouponName
            holder.binding.desc.text = list[position].arDescription
        }

        holder.binding.apply.setOnClickListener {
            onClickListener.onClickItem(position)
        }
    }
}