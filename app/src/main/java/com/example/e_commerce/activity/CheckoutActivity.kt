package com.example.e_commerce.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.e_commerce.MainActivity
import com.example.e_commerce.R
import com.example.e_commerce.roomDb.AppDatabase
import com.example.e_commerce.roomDb.ProductModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class CheckoutActivity : AppCompatActivity(), PaymentResultListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        val checkout = Checkout()
        checkout.setKeyID("rzp_test_jA01TPKltbFtUD")
        checkout.setImage(R.drawable.razorpay)

        val price = intent.getStringExtra("totalCost")

        try {
            val options = JSONObject()
            options.put("name", "E-Commerce")
            options.put("description", "E-Comerce app")
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg")
            options.put("theme.color", "#5062BD")
            options.put("currency", "INR")
            options.put("amount", (price!!.toInt()*100).toString()) //pass amount in currency subunits
            options.put("prefill.email", "amit.mrityunjay@gmail.com")
            options.put("prefill.contact", "9035411943")
            val retryObj = JSONObject()
            checkout.open(this, options)
        } catch (e: Exception) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this, "Payment success", Toast.LENGTH_SHORT).show()

        uploadData()
    }

    private fun uploadData() {
        val id = intent.getStringArrayListExtra("productIds")
        for(currId in id!!) {
            fetchData(currId)
        }
    }

    private fun fetchData(productId: String?) {
        val dao = AppDatabase.getInstance(this).productDao()

        Firebase.firestore.collection("products").document(productId!!)
            .get().addOnSuccessListener {

                lifecycleScope.launch(Dispatchers.IO) {
                    dao.deleteProduct(ProductModel(productId))
                }

                saveData(it.getString("productName"),
                    it.getString("productSp"),
                    productId
                )
            }
    }

    private fun saveData(productName: String?, productSp: String?, productId: String) {
        val preferences = this.getSharedPreferences("user", MODE_PRIVATE)
        val data = hashMapOf<String, Any>()
        data["name"] = productName!!
        data["price"] = productSp!!
        data["productId"] = productId
        data["status"] = "Ordered"
        data["userId"] = preferences.getString("number", "")!!

        val firestore = Firebase.firestore.collection("allOrders")
        val key = firestore.document().id
        data["orderId"] = key

        firestore.document(key).set(data).addOnSuccessListener {
            Toast.makeText(this, "Order Placed", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
            .addOnFailureListener {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this, "Payment error", Toast.LENGTH_SHORT).show()
    }
}