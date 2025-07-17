package com.app.delmon.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.delmon.R
import com.app.delmon.databinding.ProductSizeCardBinding
import com.app.delmon.interfaces.OnClickListener

class DefaultAmountListAdapter(
    var context: Context,
    var list: ArrayList<String>,
    var onClickListener: OnClickListener,
): RecyclerView.Adapter<DefaultAmountListAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: ProductSizeCardBinding = ProductSizeCardBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.product_size_card,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        Log.d("TAG", "getItemCount: ${list.size}")
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("stringArray", "onBindViewHolder: ${list[position].toString()}")
        holder.binding.size.text = list[position].toString()
        holder.binding.root.setOnClickListener {
            onClickListener.onClickItem(position)
        }
//        holder.binding.size.setTextColor(R.color.black)
    }


}