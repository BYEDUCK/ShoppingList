package com.byeduck.shoppinglist.common.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.byeduck.shoppinglist.model.request.create.CreateShopRequest
import com.byeduck.shoppinglist.model.request.update.UpdateShopRequest
import com.byeduck.shoppinglist.repository.ShoppingRepository
import com.google.android.gms.maps.model.LatLng

class ShopsViewModel(application: Application) : AndroidViewModel(application) {

    fun getDbShopsListRef() = ShoppingRepository.getDbShopsRef()

    fun addShop(
        name: String,
        description: String,
        location: LatLng,
        radius: Double,
        isFavourite: Boolean
    ) =
        ShoppingRepository.insertShop(
            CreateShopRequest(
                name,
                description,
                location.latitude,
                location.longitude,
                radius,
                isFavourite
            )
        )

    fun updateShop(
        shopId: String,
        shopName: String,
        shopDescription: String,
        radius: Double,
        isFavourite: Boolean
    ) =
        ShoppingRepository.updateShop(
            UpdateShopRequest(
                shopId,
                shopName,
                shopDescription,
                radius,
                isFavourite
            )
        )

    fun deleteShopById(shopId: String) = ShoppingRepository.deleteShopById(shopId)

}