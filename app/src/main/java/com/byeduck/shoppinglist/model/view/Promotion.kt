package com.byeduck.shoppinglist.model.view

import java.time.LocalDate

data class Promotion(
    var id: String,
    val shopId: String,
    var shopName: String,
    var name: String,
    var shortDescription: String,
    var fullDescription: String,
    val date: LocalDate
)