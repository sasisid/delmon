package com.app.delmon.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.delmon.Model.AddressResponse
import com.app.delmon.R
import com.app.delmon.databinding.AddressCardBinding
import com.app.delmon.fragment.AddressFragment
import com.app.delmon.interfaces.OnClickListener
import com.app.delmon.interfaces.OnClickListnereWithType

class AddressAdapter(
    var context: Context,
    var data: List<AddressResponse.Data?>,
    var onClickListener: OnClickListnereWithType,
): RecyclerView.Adapter<AddressAdapter.CategoryParentViewHolder>() {


    inner class CategoryParentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: AddressCardBinding = AddressCardBinding.bind(view)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryParentViewHolder {
        return CategoryParentViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.address_card,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryParentViewHolder, position: Int) {

        holder.binding.apply {
            address.text = data[position]!!.zoneName + " , " + data[position]!!.area + " , " + data[position]!!.houseNo
            saveas.text = data[position]!!.saveAs
            if (AddressFragment.from=="cart"){
                edit.visibility = View.GONE
                delete.visibility = View.GONE
                materialDivider.visibility = View.GONE
            }

        }

        holder.binding.root.setOnClickListener {
            onClickListener.onClickItem(position,"")
        }
        holder.binding.edit.setOnClickListener {
            onClickListener.onClickItem(position,"edit")
        }
    holder.binding.delete.setOnClickListener {
        onClickListener.onClickItem(position,"delete")
    }

    }

    override fun getItemCount(): Int {
        return data.size
    }
}