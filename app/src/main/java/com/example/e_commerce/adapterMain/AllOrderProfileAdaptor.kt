package com.example.e_commerce.adapterMain

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.*
import com.example.e_commerce.databinding.AllOrderItemLayoutProfileBinding
import com.example.e_commerce.model.AllOrderModel

class AllOrderProfileAdaptor (val list: ArrayList<AllOrderModel>, val context: Context):
    Adapter<AllOrderProfileAdaptor.AllOrderProfileViewHolder>(){

    inner class AllOrderProfileViewHolder(val binding: AllOrderItemLayoutProfileBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllOrderProfileViewHolder {
        return AllOrderProfileViewHolder(
            AllOrderItemLayoutProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: AllOrderProfileViewHolder, position: Int) {
        holder.binding.productTitle.text = list[position].name
        holder.binding.productPrice.text = list[position].price

        when(list[position].status) {
            "Ordered" -> {
                holder.binding.productStatus.text = "Ordered"
            }
            "Dispatched" -> {
                holder.binding.productStatus.text = "Dispatched"
            }
            "Delivered" -> {
                holder.binding.productStatus.text = "Delivered"
            }
            "Canceled" -> {
                holder.binding.productStatus.text = "Canceled"
            }
        }
    }

}