package com.byeduck.shoppinglist.model

class PromotionModel(
    id: String = "NO-ID",
    var shopId: String = "NO-SHOP",
    var shopName: String = "NO-SHOP-NAME",
    var name: String = "NO-NAME",
    var shortDescription: String = "NO-SHORT-DESCRIPTION",
    var fullDescription: String = "NO-FULL-DESCRIPTION",
    var date: String = "NO-DATE"
) : Model(id)