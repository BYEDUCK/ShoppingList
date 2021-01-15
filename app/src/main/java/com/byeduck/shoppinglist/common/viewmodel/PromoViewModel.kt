package com.byeduck.shoppinglist.common.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.byeduck.shoppinglist.model.request.create.CreatePromoRequest
import com.byeduck.shoppinglist.model.view.Promotion
import com.byeduck.shoppinglist.repository.ShoppingRepository

class PromoViewModel(application: Application) : AndroidViewModel(application) {

    fun getDbPromosRef(shopId: String) = ShoppingRepository.getDbPromosRef(shopId)

    fun deletePromoById(promoId: String, shopId: String) =
        ShoppingRepository.deletePromoById(promoId, shopId)

    fun addPromo(
        promoName: String,
        promoShortDesc: String,
        promoFullDesc: String,
        shopId: String,
        shopName: String,
        date: String
    ) = ShoppingRepository.insertPromo(
        CreatePromoRequest(
            promoName, promoShortDesc, promoFullDesc, shopId, shopName, date
        )
    )

    fun updatePromo(promo: Promotion) = ShoppingRepository.updatePromo(promo)

}