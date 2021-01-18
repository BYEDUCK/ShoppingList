package com.byeduck.shoppinglist.common.converter

import com.byeduck.shoppinglist.model.PromotionModel
import com.byeduck.shoppinglist.model.ShopModel
import com.byeduck.shoppinglist.model.view.Promotion
import com.byeduck.shoppinglist.model.view.Shop
import java.time.LocalDate
import java.time.format.DateTimeFormatter

sealed class ShopConverter {

    companion object {

        fun modelFromShop(shop: Shop) = ShopModel(
            shop.id,
            shop.name,
            shop.description,
            shop.latitude,
            shop.longitude,
            shop.radius,
            shop.isFavourite
        )

        fun shopFromModel(model: ShopModel) = Shop(
            model.id,
            model.name,
            model.description,
            model.latitude,
            model.longitude,
            model.radius,
            model.favourite
        )

        fun modelFromPromotion(promo: Promotion) = PromotionModel(
            promo.id,
            promo.shopId,
            promo.shopName,
            promo.name,
            promo.shortDescription,
            promo.fullDescription,
            promo.date.toString()
        )

        fun promotionFromModel(model: PromotionModel) = Promotion(
            model.id,
            model.shopId,
            model.shopName,
            model.name,
            model.shortDescription,
            model.fullDescription,
            LocalDate.parse(model.date, DateTimeFormatter.ISO_DATE)
        )

    }

}