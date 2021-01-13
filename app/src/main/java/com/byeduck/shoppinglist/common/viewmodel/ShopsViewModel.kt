package com.byeduck.shoppinglist.common.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.byeduck.shoppinglist.model.request.CreateShopRequest
import com.byeduck.shoppinglist.model.view.Shop
import com.byeduck.shoppinglist.repository.ShoppingRepository
import com.google.android.gms.maps.model.LatLng

class ShopsViewModel(application: Application) : AndroidViewModel(application) {

    fun getDbShopsListRef() = ShoppingRepository.getDbShopsRef()

    fun addShop(name: String, description: String, location: LatLng, radius: Double) =
        ShoppingRepository.insertShop(
            CreateShopRequest(name, description, location.latitude, location.longitude, radius)
        )

    fun updateShop(shop: Shop) = ShoppingRepository.updateShop(shop)

    fun deleteShopById(shopId: String) = ShoppingRepository.deleteShopById(shopId)

}