package com.example.e_commerce.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.e_commerce.MainActivity
import com.example.e_commerce.databinding.ActivityProductDetailsBinding
import com.example.e_commerce.roomDb.AppDatabase
import com.example.e_commerce.roomDb.ProductDao
import com.example.e_commerce.roomDb.ProductModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)

        getProductDetails(intent.getStringExtra("id"))

        setContentView(binding.root)
    }

    private fun getProductDetails(productId: String?) {
        Firebase.firestore.collection("products").document(productId!!)
            .get().addOnSuccessListener {
                val list = it.get("productImages") as ArrayList<String>

                val name = it.getString("productName")
                val productSp = it.getString("productSp")
                val productDesc = it.getString("productDescription")

                binding.textView10.text = name
                binding.textView11.text = productSp
                binding.textView12.text = productDesc

                val slideList = ArrayList<SlideModel>()
                for(data in list){
                    slideList.add(SlideModel(data, ScaleTypes.CENTER_CROP))
                }

                cartAction(productId, name, productSp, it.getString("productCoverImg"))

                binding.imageSlider.setImageList(slideList)

        }.addOnFailureListener{
            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cartAction(productId: String, name: String?, productSp: String?, coverImg: String?) {
        val productDao = AppDatabase.getInstance(this).productDao()

        if(productDao.isExist(productId) != null) {
            binding.button8.text = "Go to cart"
        } else {
            binding.button8.text = "Add to cart"
        }

        binding.button8.setOnClickListener {
            if(productDao.isExist(productId) != null) {
                openCart()
            } else {
                addToCart(productDao, productId, name, productSp, coverImg)
            }
        }
    }

    private fun openCart() {
        val preference = this.getSharedPreferences("info", MODE_PRIVATE)
        val editor = preference.edit()
        editor.putBoolean("isCart", true)
        editor.apply()

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun addToCart(productDao: ProductDao, productId: String, name: String?, productSp: String?, coverImg: String?) {
        val data = ProductModel(productId, name, coverImg, productSp)
        lifecycleScope.launch(Dispatchers.IO) {
            productDao.insertProduct(data)
            binding.button8.text = "Go to cart"
        }
    }
}