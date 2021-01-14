package com.byeduck.shoppinglist.common.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.byeduck.shoppinglist.model.request.CreatePromoRequest
import com.byeduck.shoppinglist.model.view.Promotion
import com.byeduck.shoppinglist.repository.ShoppingRepository

class PromoViewModel(application: Application) : AndroidViewModel(application) {

    fun getDbPromosRef() = ShoppingRepository.getDbPromosRef()

    fun deletePromoById(promoId: String) = ShoppingRepository.deletePromoById(promoId)

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