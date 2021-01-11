package com.byeduck.shoppinglist.map.shops

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.byeduck.shoppinglist.MainActivity
import com.byeduck.shoppinglist.R
import com.byeduck.shoppinglist.common.viewmodel.ShopsViewModel
import com.byeduck.shoppinglist.databinding.ActivityShopsMapBinding
import com.byeduck.shoppinglist.map.MapsFragment
import com.byeduck.shoppinglist.map.ShopMarker
import com.byeduck.shoppinglist.model.ShopModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener

class ShopsMapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityShopsMapBinding.inflate(layoutInflater)
        val viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(ShopsViewModel::class.java)
        viewModel.getDbShopsListRef().addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val shops = snapshot.getValue(object :
                    GenericTypeIndicator<Map<String, ShopModel>>() {})?.values ?: return
                displayMapFragment(shops)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ERROR", error.message)
            }

        })
        setContentView(binding.root)
    }

    private fun displayMapFragment(shops: Collection<ShopModel>) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(
            R.id.mapPlaceholder,
            MapsFragment(shops.map { ShopMarker(it.name, LatLng(it.latitude, it.longitude)) })
        )
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        val goToMainIntent = Intent(applicationContext, MainActivity::class.java)
        startActivity(goToMainIntent)
    }
}