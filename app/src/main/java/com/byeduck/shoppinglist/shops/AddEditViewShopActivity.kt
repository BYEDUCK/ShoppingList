package com.byeduck.shoppinglist.shops

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.byeduck.shoppinglist.R
import com.byeduck.shoppinglist.common.converter.ShopConverter
import com.byeduck.shoppinglist.common.viewmodel.ShopsViewModel
import com.byeduck.shoppinglist.databinding.ActivityAddEditViewShopBinding
import com.byeduck.shoppinglist.map.GeofenceReceiver
import com.byeduck.shoppinglist.map.MapsFragment
import com.byeduck.shoppinglist.map.ShopMarker
import com.byeduck.shoppinglist.model.ShopModel
import com.byeduck.shoppinglist.model.view.Shop
import com.byeduck.shoppinglist.repository.ShoppingRepository
import com.byeduck.shoppinglist.shops.promo.PromoActivity
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson

class AddEditViewShopActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditViewShopBinding
    private lateinit var viewModel: ShopsViewModel
    private lateinit var location: LatLng
    private lateinit var geoClient: GeofencingClient
    private var shopId = ""
    private var shopName = ""
    private var viewOnly = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        geoClient = LocationServices.getGeofencingClient(this)
        binding = ActivityAddEditViewShopBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(ShopsViewModel::class.java)
        setContentView(binding.root)
        val shopJson = intent?.getStringExtra("shop") ?: ""
        val shopId = intent?.getStringExtra("shopId") ?: ""
        viewOnly = intent?.getBooleanExtra("view", false) ?: false
        if (shopJson.isEmpty() && shopId.isEmpty()) {
            val latitude = intent?.getDoubleExtra("latitude", 0.0) ?: 0.0
            val longitude = intent?.getDoubleExtra("longitude", 0.0) ?: 0.0
            location = LatLng(latitude, longitude)
            binding.locationTxt.text = getString(R.string.latitude_longitude, latitude, longitude)
            binding.shopRadiusTxt.setText(getString(R.string.default_radius))
            displayMapFragment()
        } else {
            if (shopJson.isNotEmpty()) {
                val shop = Gson().fromJson(shopJson, Shop::class.java)
                populateFields(shop)
            } else {
                ShoppingRepository.getDbShopsRef()
                    .child(shopId)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val shopModel = snapshot.getValue(ShopModel::class.java) ?: return
                            val shop = ShopConverter.shopFromModel(shopModel)
                            populateFields(shop)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.e("ADD/EDIT/VIEW SHOP", "ERROR", error.toException())
                            goBackToShops()
                        }

                    })
            }
        }
    }

    override fun onBackPressed() {
        goBackToShops()
    }

    fun goToPromotions(ignored: View) {
        val goToPromotionsIntent = Intent(this, PromoActivity::class.java).apply {
            putExtra("shopId", shopId)
            putExtra("shopName", shopName)
        }
        startActivity(goToPromotionsIntent)
    }

    fun addEditShop(ignored: View) {
        val shopName = binding.shopNameTxt.text.toString()
        val description = binding.shopDescriptionTxt.text.toString()
        val radius = binding.shopRadiusTxt.text.toString().toDouble()
        val isFavourite = binding.isFavCheckbox.isChecked
        if (shopId.isEmpty()) {
            viewModel
                .addShop(shopName, description, location, radius, isFavourite)
                .addOnSuccessListener {
                    setGeoFence(it, radius)
                    goBackToShops()
                }
                .addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
        } else {
            viewModel
                .updateShop(
                    shopId, shopName, description, radius, isFavourite
                )
                .addOnSuccessListener {
                    removeGeofencesForShop(shopId) // remove old
                    setGeoFence(shopId, radius)
                    goBackToShops()
                }
                .addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
        }
    }

    fun cancel(ignored: View) {
        goBackToShops()
    }

    private fun populateFields(shop: Shop) {
        shopName = shop.name
        this.shopId = shop.id
        location = LatLng(shop.latitude, shop.longitude)
        binding.apply {
            shopNameTxt.setText(shop.name)
            shopDescriptionTxt.setText(shop.description)
            shopRadiusTxt.setText(shop.radius.toString())
            locationTxt.text =
                getString(R.string.latitude_longitude, shop.latitude, shop.longitude)
            isFavCheckbox.isChecked = shop.isFavourite
        }
        if (viewOnly) {
            binding.apply {
                shopNameTxt.isEnabled = false
                shopDescriptionTxt.isEnabled = false
                shopRadiusTxt.isEnabled = false
                isFavCheckbox.isEnabled = false
                actionButtons.visibility = View.INVISIBLE
                promoBtn.visibility = View.VISIBLE
            }
        }
        displayMapFragment(shop.name)
    }

    private fun removeGeofencesForShop(shopId: String) {
        val geoIdEnter = "${shopId}_enter"
        val geoIdExit = "${shopId}_exit"
        geoClient.removeGeofences(listOf(geoIdEnter, geoIdExit))
            .addOnSuccessListener {
                Toast.makeText(
                    this,
                    "Geofence removed : ${geoIdEnter}|${geoIdExit}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener {
                Log.e("GEOFENCE", "NOT REMOVED: ${geoIdEnter}|${geoIdExit}", it)
            }
    }

    @SuppressLint("MissingPermission")
    private fun setGeoFence(shopId: String, radius: Double) {
        val geoIdEnter = "${shopId}_enter"
        val geoIdExit = "${shopId}_exit"
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
                Log.d(
                    "GEOFENCE",
                    "Added geofence ${geoIdEnter}|${geoIdExit} for location $location and radius $radius"
                )
                Toast.makeText(
                    this,
                    "Geofence added : ${geoIdEnter}|${geoIdExit}",
                    Toast.LENGTH_SHORT
                ).show()
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