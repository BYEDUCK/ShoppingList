package com.byeduck.shoppinglist.shops

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.byeduck.shoppinglist.R
import com.byeduck.shoppinglist.common.viewmodel.ShopsViewModel
import com.byeduck.shoppinglist.databinding.ActivityAddEditViewShopBinding
import com.byeduck.shoppinglist.map.GeofenceReceiver
import com.byeduck.shoppinglist.map.MapsFragment
import com.byeduck.shoppinglist.map.ShopMarker
import com.byeduck.shoppinglist.model.view.Shop
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import java.util.*

class AddEditViewShopActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditViewShopBinding
    private lateinit var viewModel: ShopsViewModel
    private lateinit var location: LatLng
    private var shopId = ""

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
            binding.shopRadiusTxt.setText(getString(R.string.default_radius))
            displayMapFragment()
        } else {
            val shop = Gson().fromJson(shopJson, Shop::class.java)
            shopId = shop.id
            location = LatLng(shop.latitude, shop.longitude)
            binding.shopNameTxt.setText(shop.name)
            binding.shopDescriptionTxt.setText(shop.description)
            binding.shopRadiusTxt.setText(shop.radius.toString())
            binding.locationTxt.text =
                getString(R.string.latitude_longitude, shop.latitude, shop.longitude)
            if (viewOnly) {
                binding.shopNameTxt.isEnabled = false
                binding.shopDescriptionTxt.isEnabled = false
                binding.shopRadiusTxt.isEnabled = false
                binding.actionButtons.visibility = View.INVISIBLE
            }
            displayMapFragment(shop.name)
        }
    }

    override fun onBackPressed() {
        goBackToShops()
    }

    fun addEditShop(ignored: View) {
        val shopName = binding.shopNameTxt.text.toString()
        val description = binding.shopDescriptionTxt.text.toString()
        val radius = binding.shopRadiusTxt.text.toString().toDouble()
        if (shopId.isEmpty()) {
            viewModel.addShop(shopName, description, location, radius)
        } else {
            viewModel.updateShop(
                Shop(
                    shopId, shopName, description, location.latitude, location.longitude, radius
                )
            )
        }
        setGeoFence(shopName, radius)
        goBackToShops()
    }

    fun cancel(ignored: View) {
        goBackToShops()
    }

    @SuppressLint("MissingPermission")
    private fun setGeoFence(shopName: String, radius: Double) {
        val hash = Objects.hash(shopName, location).toString(16)
        val geoIdEnter = "$hash-enter"
        val geoIdExit = "$hash-exit"
        val geoClient = LocationServices.getGeofencingClient(this)
        geoClient.removeGeofences(listOf(geoIdEnter, geoIdExit))
        val geofenceEnter = Geofence.Builder().setRequestId(geoIdEnter)
            .setCircularRegion(location.latitude, location.longitude, radius.toFloat())
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .build()
        val geofenceExit = Geofence.Builder().setRequestId(geoIdExit)
            .setCircularRegion(location.latitude, location.longitude, radius.toFloat())
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_EXIT)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .build()
        val request = geofencesToRequest(geofenceEnter, geofenceExit)
        geoClient.addGeofences(request, getGeofencePendingIntent())
            .addOnSuccessListener {
                Toast.makeText(this, "Geofence added", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Geofence not added", Toast.LENGTH_SHORT).show()
            }
    }

    private fun getGeofencePendingIntent() = PendingIntent.getBroadcast(
        this, 0, Intent(this, GeofenceReceiver::class.java),
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    private fun geofencesToRequest(vararg geofences: Geofence) = GeofencingRequest.Builder()
        .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
        .addGeofences(listOf(*geofences))
        .build()

    private fun displayMapFragment(markerTitle: String = "My location") {
        val radius = binding.shopRadiusTxt.text.toString().toDouble()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(
            R.id.mapPlaceholder,
            MapsFragment(listOf(ShopMarker(markerTitle, location, radius)))
        )
        fragmentTransaction.commit()
    }

    private fun goBackToShops() {
        val intent = Intent(applicationContext, ShopsActivity::class.java)
        startActivity(intent)
    }
}