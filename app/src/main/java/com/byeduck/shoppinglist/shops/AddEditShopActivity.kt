package com.byeduck.shoppinglist.shops

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.byeduck.shoppinglist.R
import com.byeduck.shoppinglist.databinding.ActivityAddEditShopBinding

class AddEditShopActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAddEditShopBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val latitude = intent?.getDoubleExtra("latitude", 0.0) ?: 0.0
        val longitude = intent?.getDoubleExtra("longitude", 0.0) ?: 0.0
        binding.locationTxt.text = getString(R.string.latitude_longitude, latitude, longitude)
        val shopId = intent?.getStringExtra("shopId") ?: ""
    }

    fun addEditShop(ignored: View) {}

    fun cancel(ignored: View) {
        val intent = Intent(applicationContext, ShopsActivity::class.java)
        startActivity(intent)
    }
}