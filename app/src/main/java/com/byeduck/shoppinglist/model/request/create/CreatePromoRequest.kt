package com.byeduck.shoppinglist.model.request.create

data class CreatePromoRequest(
    val promoName: String,
    val promoShortDesc: String,
    val promoFullDesc: String,
    val shopId: String,
    val shopName: String,
    val date: String
)