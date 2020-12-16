package com.byeduck.shoppinglist.model

import com.google.firebase.database.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
class ShoppingListModel(
    id: String = "NO-ID",
    var name: String = "NO-NAME",
    var createdAt: Long = Date().time,
    var updatedAt: Long = Date().time
) : Model(id)