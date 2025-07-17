package com.app.delmon.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.delmon.Model.OrderListResponse
import com.app.delmon.R
import com.app.delmon.activity.OrderDetailsActivity
import com.app.delmon.databinding.CardOrderListBinding
import com.app.delmon.interfaces.DialogCallBack
import com.app.delmon.interfaces.OnClickListnereWithType
import com.app.delmon.utils.BaseUtils
import com.app.delmon.utils.DialogUtils

class OrderListAdapter(
    var context: Context, var list: List<OrderListResponse.OrderData>,
    var onClickListener: OnClickListnereWithType,
    var type:Int): RecyclerView.Adapter<OrderListAdapter.ViewHolder>(){
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: CardOrderListBinding = CardOrderListBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(
                R.layout.card_order_list,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.orderId.text = "Order ID : "+list[position].orderId
        if (list[position].orderStatus=="PLACED"){
            holder.binding.adminApproveTxt.visibility = View.VISIBLE
            holder.binding.stepper.visibility = View.GONE
            holder.binding.acceptBtn.visibility = View.GONE
            holder.binding.rejectBtn.visibility = View.GONE
            holder.binding.orderStatus.text = context.resources.getText(R.string.order_placed)
            holder.binding.placedDate.text = BaseUtils.getFormatedDateUtc(list[position].orderPlaceTime!!,"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'","EEEE, MMM d, yyyy")
        }
        else if (list[position].orderStatus=="APPROVE"){
            holder.binding.orderStatus.text = context.resources.getText(R.string.order_placed)
            holder.binding.placedDate.text = BaseUtils.getFormatedDateUtc(list[position].orderPlaceTime!!,"yyyy-MM-dd'T'HH:mm:ss.SSS","EEEE, MMM d, yyyy")
            holder.binding.deliverStatus.visibility = View.VISIBLE
            holder.binding.deliverDate.visibility = View.VISIBLE
            holder.binding.deliverDate.text = list[position].deliveryDate!!
            holder.binding.adminApproveTxt.visibility = View.GONE
            holder.binding.stepper.visibility = View.GONE
            holder.binding.acceptBtn.visibility = View.VISIBLE
            holder.binding.rejectBtn.visibility = View.VISIBLE
            holder.binding.acceptBtn.setOnClickListener {
                onClickListener.onClickItem(position,"USERACCEPTED")
            }
            holder.binding.rejectBtn.setOnClickListener {
                DialogUtils.showDialog(context,"","Are you Sure to Cancel the Order?",context.resources.getString(R.string.ok),context.resources.getString(R.string.cancel),object :
                    DialogCallBack {
                    override fun onPositiveClick(toString: String) {
                        onClickListener.onClickItem(position,"USERREJECTED")
                    }

                    override fun onNegativeClick() {
                    }

                })
            }
        } else if (list[position].orderStatus=="USERACCEPTED"){
            holder.binding.orderStatus.text = context.resources.getText(R.string.delivery_on)
            holder.binding.placedDate.text = list[position].deliveryDate!!
            holder.binding.adminApproveTxt.visibility = View.GONE
            holder.binding.stepper.visibility = View.VISIBLE
            holder.binding.acceptBtn.visibility = View.GONE
            holder.binding.rejectBtn.visibility = View.GONE
        }
        else if (list[position].orderStatus=="OUTFORDELIVERY"){
            holder.binding.orderStatus.text = context.resources.getText(R.string.delivery_on)
            holder.binding.placedDate.text = list[position].deliveryDate!!
            holder.binding.adminApproveTxt.visibility = View.GONE
            holder.binding.stepper.visibility = View.VISIBLE
            holder.binding.ouForDelivery.setImageDrawable(context.resources.getDrawable(R.drawable.order_confirmed));

            holder.binding.acceptBtn.visibility = View.GONE
            holder.binding.rejectBtn.visibility = View.GONE

        }

        else if (list[position].orderStatus=="COMPLETED"){
            holder.binding.orderStatus.setTextColor(context.resources.getColor(R.color.secondary_feeding_color));
            holder.binding.placedDate.setTextColor(context.resources.getColor(R.color.secondary_feeding_color));
            holder.binding.orderStatus.text = context.resources.getText(R.string.delivered_on)
            holder.binding.placedDate.text = BaseUtils.getFormatedDateUtc(list[position].updatedAt!!,"yyyy-MM-dd'T'HH:mm:ss.SSSZZZ","EEEE, MMM d, yyyy")
            holder.binding.adminApproveTxt.visibility = View.GONE
            holder.binding.stepper.visibility = View.GONE
            holder.binding.acceptBtn.visibility = View.GONE
            holder.binding.rejectBtn.visibility = View.GONE
        }
        else if (list[position].orderStatus=="USERREJECTED"){
            holder.binding.orderStatus.setTextColor(context.resources.getColor(R.color.secondary_color));
            holder.binding.placedDate.setTextColor(context.resources.getColor(R.color.secondary_color));

//            holder.binding.orderStatus.setTextColor(R.color.secondary_color)
//            holder.binding.placedDate.setTextColor(R.color.secondary_color)
            holder.binding.orderStatus.text = context.resources.getText(R.string.cancelled_on)
            holder.binding.placedDate.text = BaseUtils.getFormatedDateUtc(list[position].updatedAt!!,"yyyy-MM-dd'T'HH:mm:ss.SSSZZZ","EEEE, MMM d, yyyy")
        }

        if (list[position].deliveryType=="PICKUP"){
            holder.binding.outForText.text = context.resources.getText(R.string.ready_to_pickup)
        }
    /*    if(type == 3){
            holder.binding.orderStatus.text = "Cancelled at"
            if(list[position].cancelTime != null){
                holder.binding.placedDate.text = BaseUtils.getFormatedDateUtc(list[position].cancelTime!!,"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'","dd/MM/yyyy")
            }
        }
        else{
            holder.binding.orderStatus.text = context.getString(R.string.order_placed)
            if(list[position].orderPlaceTime != null){
                holder.binding.placedDate.text = BaseUtils.getFormatedDateUtc(list[position].orderPlaceTime!!,"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'","dd/MM/yyyy")
            }
        }*/
        holder.binding.amt.text = String.format("%.2f", list[position].netAmount!!.toDouble())+" BD"
        holder.binding.orderDetails.setOnClickListener {
            val intent = Intent(context, OrderDetailsActivity::class.java)
            intent.putExtra("key",list[position].id.toString())
            context.startActivity(intent)
        }
    }
}