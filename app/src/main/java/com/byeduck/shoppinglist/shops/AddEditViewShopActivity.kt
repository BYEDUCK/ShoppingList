package com.byeduck.shoppinglist.shops

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.byeduck.shoppinglist.R
import com.byeduck.shoppinglist.common.viewmodel.ShopsViewModel
import com.byeduck.shoppinglist.databinding.ActivityAddEditViewShopBinding
import com.byeduck.shoppinglist.model.view.Shop
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson

class AddEditViewShopActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditViewShopBinding
    private lateinit var viewModel: ShopsViewModel
    private lateinit var location: LatLng
    private var shopId = ""
    private var viewOnly = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditViewShopBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(ShopsViewModel::class.java)
        setContentView(binding.root)
        val shopJson = intent?.getStringExtra("shop") ?: ""
        val viewOnly = intent?.getBooleanExtra("view", false) ?: false
        if (shopJson.isEmpty()) {
            val latitude = intent?.getDoubleExtra("latitude", 0.0) ?: 0.0
            val longitude = intent?.getDoubleExtra("longitude", 0.0) ?: 0.0
            location = LatLng(latitude, longitude)
            binding.locationTxt.text = getString(R.string.latitude_longitude, latitude, longitude)
        } else {
            val shop = Gson().fromJson(shopJson, Shop::class.java)
            shopId = shop.id
            binding.shopNameTxt.setText(shop.name)
            binding.shopDescriptionTxt.setText(shop.description)
            binding.locationTxt.text =
                getString(R.string.latitude_longitude, shop.latitude, shop.longitude)
            if (viewOnly) {
                binding.shopNameTxt.isEnabled = false
                binding.shopDescriptionTxt.isEnabled = false
                binding.actionButtons.visibility = View.INVISIBLE
            }
        }

    }

    override fun onBackPressed() {
        goBackToShops()
    }

    fun addEditShop(ignored: View) {
        val shopName = binding.shopNameTxt.text.toString()
        val description = binding.shopDescriptionTxt.text.toString()
        if (shopId.isNotEmpty()) {
            viewModel.addShop(shopName, description, location)
        } else {
            viewModel.updateShop(
                Shop(
                    shopId, shopName, description, location.latitude, location.longitude
                )
            )
        }
        goBackToShops()
    }

    fun cancel(ignored: View) {
        goBackToShops()
    }

    private fun goBackToShops() {
        val intent = Intent(applicationContext, ShopsActivity::class.java)
        startActivity(intent)
    }
}