package com.app.delmon.adapter

import com.app.delmon.databinding.CartProductLayoutBinding

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.delmon.Model.CartResponseNew
import com.app.delmon.Model.OrderListResponse
import com.app.delmon.Model.ProductResponse
import com.app.delmon.R
import com.app.delmon.fragment.CartFragment
import com.app.delmon.interfaces.OnClickListnereWithType

class CartAdapter(
    var type:Int,
    var context: Context,
    var data: ArrayList<CartResponseNew.Data?>,
    var productData: ArrayList<OrderListResponse.ProductData>,
    var onClickListener: OnClickListnereWithType,
): RecyclerView.Adapter<CartAdapter.CategoryParentViewHolder>() {

    inner class CategoryParentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: CartProductLayoutBinding = CartProductLayoutBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryParentViewHolder {
        return CategoryParentViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.cart_product_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryParentViewHolder, position: Int) {
        if(type == 0){
            holder.binding.root.setOnClickListener {
                onClickListener.onClickItem(position,"root")
            }
            var cartCount = data[position]!!.quantity
            holder.binding.apply {
                title.text = data[position]!!.name
                if (data[position]!!.cartonActive==1) {
                    pieces.text = " (${data[position]!!.noOfPieces.toString()} ${context.resources.getString(R.string.pieces)})"
                } else {
                    pieces.visibility = View.GONE
                }
                if (data[position]!!.weight!=null){
                    weight.text = data[position]!!.weight.toString()
                }
                var tot = data[position]!!.price!! * data[position]!!.quantity!!
                amt.text = "${data[position]!!.price!!} BD"

                count.text = cartCount.toString()
                if (data[position]!!.vat != null && data[position]!!.vat != 0 ){
                    productVat.text = "VAT: "+data[position]!!.vat.toString()+ "%"
                }else{
                    productVat.visibility= View.GONE
                }
                // CartFragment.cartPrice.value = CartFragment.cartPrice.value!! + data[position]!!.price!!
                CartFragment.totamt = CartFragment.totamt+tot
                plus.setOnClickListener {
                    data[position]!!.quantity = cartCount!! +1
                    // CartFragment.cartPrice.value = CartFragment.cartPrice.value!! + data[position]!!.price!!
                    onClickListener.onClickItem(position,"add")
                    notifyDataSetChanged()
                }
                minus.setOnClickListener {
                    data[position]!!.quantity = cartCount!! -1
//                amt.text = "BD "+(data[position]!!.price!! * data[position]!!.quantity!!)
                    //  CartFragment.cartPrice.value = CartFragment.cartPrice.value!! + data[position]!!.price!!
                    onClickListener.onClickItem(position,"sub")
                    if ((cartCount!! -1)==0){
                        data.removeAt(position)
                    }
                    notifyDataSetChanged()
                }
            }

        }
        else {
            holder.binding.line.visibility = View.INVISIBLE
            holder.binding.constraintLayout5.visibility = View.GONE
            holder.binding.apply {
                title.text = productData[position].enProductName
                weight.text = productData[position].weight
                amt.text = "${productData[position]!!.price!!} BD"
                if (productData[position].vat != null){
                    productVat.text = "VAT: "+productData[position]!!.vat.toString()+ "%"
                }
                else{
                    productVat.visibility= View.GONE
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return if(type == 0){
            data.size
        } else{
            productData.size
        }
    }
}