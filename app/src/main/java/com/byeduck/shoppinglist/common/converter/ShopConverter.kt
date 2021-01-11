package com.byeduck.shoppinglist.common.converter

import com.byeduck.shoppinglist.model.ShopModel
import com.byeduck.shoppinglist.model.view.Shop

sealed class ShopConverter {

    companion object {

        fun modelFromShop(shop: Shop) = ShopModel(
            shop.id, shop.name, shop.description, shop.latitude, shop.longitude
        )

        fun shopFromModel(model: ShopModel) = Shop(
            model.id, model.name, model.description, model.latitude, model.longitude
        )

    }

}