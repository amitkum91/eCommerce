package com.example.e_commerce.adapterMain

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_commerce.activity.ProductDetailsActivity
import com.example.e_commerce.databinding.LayoutProductItemBinding
import com.example.e_commerce.model.AddProductModel

class ProductAdaptor(val context: Context, val list: ArrayList<AddProductModel>)
    :RecyclerView.Adapter<ProductAdaptor.ProductViewHolder>() {

    inner class ProductViewHolder(val binding: LayoutProductItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = LayoutProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val data = list[position]
        Glide.with(context).load(data.productCoverImg).into(holder.binding.imageView4)
        holder.binding.textView4.text = data.productName
        holder.binding.textView5.text = data.productCategory
        holder.binding.textView6.text = data.productMrp

        holder.binding.button5.text = data.productSp
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra("id", list[position].productId)
            context.startActivity(intent)
        }
    }
}