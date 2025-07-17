package com.app.delmon.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.delmon.Model.CouponListModel
import com.app.delmon.R
import com.app.delmon.databinding.CardBillingListBinding
import org.json.JSONArray

class BillingAdapter(var context: Context, var list: JSONArray): RecyclerView.Adapter<BillingAdapter.ViewHolder>(){
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: CardBillingListBinding = CardBillingListBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.card_billing_list,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.length()
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.title.text = list.getJSONObject(position).getString("title")
        if (!list.getJSONObject(position).getString("price").isNullOrEmpty() && !list.getJSONObject(position).getString("price").equals("null",true)){
            holder.binding.price.text = list.getJSONObject(position).getString("price")
        }

        if(position == list.length()-1){
            holder.binding.lineView.visibility = View.VISIBLE
        }
        else{
            holder.binding.lineView.visibility = View.GONE
        }
    }
}