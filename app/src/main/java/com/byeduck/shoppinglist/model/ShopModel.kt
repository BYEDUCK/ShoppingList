package com.byeduck.shoppinglist.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class ShopModel(
    id: String = "NO-ID",
    val name: String = "NO-NAME",
    var description: String = "NO-DESCRIPTION",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var radius: Double = 50.0,
    var favourite: Boolean = false
) : Model(id) {
}