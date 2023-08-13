package com.example.e_commerce.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView.*
import com.example.e_commerce.databinding.AllOrderItemLayoutBinding
import com.example.e_commerce.model.AllOrderModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AllOrdersAdaptor (val list: ArrayList<AllOrderModel>, val context: Context): Adapter<AllOrdersAdaptor.AllOrderViewHolder>(){

    inner class AllOrderViewHolder(val binding: AllOrderItemLayoutBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllOrderViewHolder {
        return AllOrderViewHolder(
            AllOrderItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: AllOrderViewHolder, position: Int) {
        holder.binding.productTitle.text = list[position].name
        holder.binding.productPrice.text = list[position].price
        holder.binding.cancelBtn.setOnClickListener {
            holder.binding.proceedBtn.visibility = GONE
            holder.binding.cancelBtn.text = "Canceled"
            updateStatus("Canceled", list[position].orderId!!)
        }

        when(list[position].status) {
            "Ordered" -> {
                holder.binding.proceedBtn.text = "Dispatched"
                holder.binding.proceedBtn.setOnClickListener {
                    updateStatus("Dispatched", list[position].orderId!!)
                }
            }
            "Dispatched" -> {
                holder.binding.proceedBtn.text = "Delivered"
                holder.binding.proceedBtn.setOnClickListener {
                    updateStatus("Delivered", list[position].orderId!!)
                }
            }
            "Delivered" -> {
                holder.binding.cancelBtn.visibility = GONE
                holder.binding.proceedBtn.text = "Already Delivered"
                holder.binding.proceedBtn.isEnabled = false
            }
            "Canceled" -> {
                holder.binding.proceedBtn.visibility = GONE
                holder.binding.cancelBtn.isEnabled = false
            }
        }
    }

    fun updateStatus(str: String, doc: String) {
        val data = hashMapOf<String, Any>()
        data["status"] = str
        Firebase.firestore.collection("allOrders")
            .document(doc).update(data).addOnSuccessListener {
                Toast.makeText(context, "Status Updated", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
    }
}