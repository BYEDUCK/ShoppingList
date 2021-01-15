package com.byeduck.shoppinglist.map

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.byeduck.shoppinglist.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment(
    private val shops: List<ShopMarker>
) : Fragment() {

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        shops.forEach {
            Log.d("MARKER ADD", "${it.shopName} -> ${it.location}")
            googleMap.addMarker(
                MarkerOptions()
                    .position(it.location)
                    .title(it.shopName)
            )
            googleMap.addCircle(
                CircleOptions()
                    .fillColor(R.color.circle_blue)
                    .strokeColor(R.color.circle_dark_blue)
                    .center(it.location)
                    .radius(it.radius)
            )
        }
        googleMap.isMyLocationEnabled = true
        // zoom to first marker
        // https://stackoverflow.com/questions/16458900/google-maps-api-v2-zooming-near-the-marker
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(shops[0].location, 15F))
        googleMap.animateCamera(CameraUpdateFactory.zoomIn())
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15F), 2000, null)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}