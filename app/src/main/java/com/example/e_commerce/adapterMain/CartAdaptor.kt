package com.example.e_commerce.adapterMain

import android.content.Context
import android.content.Intent
import android.provider.Settings.Global
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.e_commerce.activity.ProductDetailsActivity
import com.example.e_commerce.databinding.LayoutCartItemBinding
import com.example.e_commerce.roomDb.AppDatabase
import com.example.e_commerce.roomDb.ProductModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CartAdaptor(val context: Context, val list: List<ProductModel>): Adapter<CartAdaptor.CartViewHolder>() {

    inner class CartViewHolder(val binding: LayoutCartItemBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = LayoutCartItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return CartViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        Glide.with(context).load(list[position].productImage).into(holder.binding.imageView6)
        val pos = list[position]
        holder.binding.textView9.text = pos.productName
        holder.binding.textView13.text = pos.productSp

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra("id", list[position].productId)
            context.startActivity(intent)
        }

        val dao = AppDatabase.getInstance(context).productDao()
        holder.binding.imageView7.setOnClickListener {

            GlobalScope.launch(Dispatchers.IO) {
                dao.deleteProduct(ProductModel(pos.productId, pos.productName, pos.productImage, pos.productSp))
            }
        }
    }
}