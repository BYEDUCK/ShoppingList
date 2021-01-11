package com.byeduck.shoppinglist.common.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.byeduck.shoppinglist.model.request.CreateShopRequest
import com.byeduck.shoppinglist.model.view.Shop
import com.byeduck.shoppinglist.repository.ShoppingRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShopsViewModel(application: Application) : AndroidViewModel(application) {

    fun getDbShopsListRef() = ShoppingRepository.getDbShopsRef()

    fun addShop(name: String, description: String, location: LatLng) =
        viewModelScope.launch(Dispatchers.IO) {
            ShoppingRepository.insertShop(
                CreateShopRequest(name, description, location.latitude, location.longitude)
            )
        }

    fun updateShop(shop: Shop) = viewModelScope.launch(Dispatchers.IO) {
        ShoppingRepository.updateShop(shop)
    }

    fun deleteShopById(shopId: String) = ShoppingRepository.deleteShopById(shopId)

}