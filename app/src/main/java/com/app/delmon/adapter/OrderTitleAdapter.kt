package com.app.delmon.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.delmon.R
import com.app.delmon.databinding.CategoryCardBinding
import com.app.delmon.databinding.OrderTitleCardBinding
import com.app.delmon.interfaces.OnClickListener
import java.util.Locale


class OrderTitleAdapter(
    var context: Context,
    var onClickListener: OnClickListener,
    var positiontofix: Int,
    ): RecyclerView.Adapter<OrderTitleAdapter.OrderParentViewHolder>(){
    var row_index: Int = -1
    val titleString: Array<String> = if (Locale.getDefault().language == "ar") {
        arrayOf("طلبي", "الطلبات المؤكدة", "تم إكمال الطلب", "الطلبات الملغاة")
    } else {
        arrayOf("My Orders", "Confirmed Orders", "Completed Orders", "Cancelled Orders")
    }

    inner class OrderParentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: OrderTitleCardBinding = OrderTitleCardBinding.bind(view)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderParentViewHolder {
        return OrderParentViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.order_title_card,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: OrderParentViewHolder, position: Int) {
        row_index = positiontofix

        holder.binding.orderTitle.text = titleString[position]
        holder.binding.root.setOnClickListener(){
            positiontofix = position
            row_index = positiontofix
            onClickListener.onClickItem(position)
            notifyDataSetChanged()

        }
        if(row_index==position){
            //  taskFragment._binding!!.categoryRecyc.smoothScrollToPosition(position)
            holder.binding.selectionImg.visibility = View.VISIBLE
        }
        else
        {
            holder.binding.selectionImg.visibility = View.GONE
//            holder.binding.orderTitle.setTextColor(Color.parseColor("#000000"));
//            holder.binding.orderTitle.setBackgroundResource(R.drawable.grey_rectangle)

        }


    }

    override fun getItemCount(): Int {
        return 4
    }


}