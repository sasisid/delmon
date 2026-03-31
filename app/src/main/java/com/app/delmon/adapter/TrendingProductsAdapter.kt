package com.app.delmon.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.delmon.Model.TrendingProductResponse
import com.app.delmon.R
import com.app.delmon.databinding.BasketProductCardBinding
import com.app.delmon.databinding.RecentItemsBinding
import com.app.delmon.interfaces.OnClickListener
import com.app.delmon.interfaces.OnClickListnereWithType

class TrendingProductsAdapter(
    var context: Context,
    var prodctresponse: ArrayList<TrendingProductResponse.Data>,
    var onClickListener: OnClickListnereWithType,
    var onRootClickListener: OnClickListener,
): RecyclerView.Adapter<TrendingProductsAdapter.RecentSearchViewHolder>() {
    var data = prodctresponse
    inner class RecentSearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: RecentItemsBinding = RecentItemsBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentSearchViewHolder {
        return RecentSearchViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.recent_items,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return if(data.size > 5) {
            5
        } else {
            data.size
        }
    }

    override fun onBindViewHolder(holder: RecentSearchViewHolder, position: Int) {
        holder.binding.apply {
            recentItem.text = data[position].name
            recentItem.setOnClickListener {
                onClickListener.onClickItem(position, "" + data[position])
            }
        }
    }
}