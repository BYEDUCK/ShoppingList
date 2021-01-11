package com.byeduck.shoppinglist.shops

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.byeduck.shoppinglist.MainActivity
import com.byeduck.shoppinglist.common.viewmodel.ShopsViewModel
import com.byeduck.shoppinglist.databinding.ActivityShopsBinding
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnSuccessListener
import java.util.*

class ShopsActivity : AppCompatActivity() {

    private val cancellationTokenSource = CancellationTokenSource()
    private val permissionReqCode = 123
    private lateinit var binding: ActivityShopsBinding
    private lateinit var viewModel: ShopsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopsBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(ShopsViewModel::class.java)
        setContentView(binding.root)
        binding.shopsRecyclerView.adapter = ShopsAdapter(viewModel, this, supportFragmentManager)
        binding.shopsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.shopsRecyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun addCurrentLocation(ignored: View) {
        val perms = arrayOf(
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(perms, permissionReqCode)
        } else {
            onSuccessGetCurrentLocation { openAddEditShopActivity(it) }
        }
    }

    @SuppressLint("MissingPermission")
    fun onSuccessGetCurrentLocation(onSuccessListener: OnSuccessListener<in Location>) {
        LocationServices
            .getFusedLocationProviderClient(this@ShopsActivity)
            .getCurrentLocation(
                LocationRequest.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
            )
            .addOnSuccessListener(onSuccessListener)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionReqCode) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                onSuccessGetCurrentLocation { openAddEditShopActivity(it) }
            } else {
                Toast.makeText(
                    this,
                    "You need to grant permission in order for this to work",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onBackPressed() {
        val goToMainIntent = Intent(this, MainActivity::class.java)
        startActivity(goToMainIntent)
    }

    override fun onDestroy() {
        cancellationTokenSource.cancel()
        val adapter = binding.shopsRecyclerView.adapter as ShopsAdapter
        adapter.removeListener()
        super.onDestroy()
    }

    private fun openAddEditShopActivity(location: Location) {
        val intent = Intent(applicationContext, AddEditViewShopActivity::class.java)
        intent.apply {
            putExtra("longitude", location.longitude)
            putExtra("latitude", location.latitude)
        }
        startActivity(intent)
    }
}