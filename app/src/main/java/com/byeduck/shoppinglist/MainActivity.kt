package com.byeduck.shoppinglist

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.byeduck.shoppinglist.lists.ShoppingListsActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun goToShoppingLists(view: View) {
        val goToShoppingListsIntent = Intent(this, ShoppingListsActivity::class.java)
        startActivity(goToShoppingListsIntent)
    }
}