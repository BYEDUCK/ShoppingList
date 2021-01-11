package com.byeduck.shoppinglist.model

class ShopModel(
    id: String = "NO-ID",
    var name: String = "NO-NAME",
    var description: String = "NO-DESCRIPTION",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
) : Model(id) {
}