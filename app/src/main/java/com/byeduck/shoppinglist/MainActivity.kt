package com.byeduck.shoppinglist

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.byeduck.shoppinglist.databinding.ActivityMainBinding
import com.byeduck.shoppinglist.lists.ShoppingListsActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun goToShoppingLists(ignored: View) {
        val goToShoppingListsIntent = Intent(this, ShoppingListsActivity::class.java)
        startActivity(goToShoppingListsIntent)
    }
}