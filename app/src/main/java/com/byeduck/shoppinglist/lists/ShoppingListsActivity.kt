package com.byeduck.shoppinglist.lists

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.byeduck.shoppinglist.databinding.ActivityShoppingListsBinding

class ShoppingListsActivity : AppCompatActivity() {

    private lateinit var shoppingListsViewModel: ShoppingListsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityShoppingListsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        shoppingListsViewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
        ).get(ShoppingListsViewModel::class.java)
        binding.shoppingListsRecycleView.adapter = ShoppingListsAdapter(this)
        binding.shoppingListsRecycleView.layoutManager = LinearLayoutManager(this)
        binding.shoppingListsRecycleView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        shoppingListsViewModel.shoppingLists.observe(this, Observer { all ->
            all?.let {
                (binding.shoppingListsRecycleView.adapter as ShoppingListsAdapter).setShoppingLists(
                    it
                )
            }
        })
        binding.addShoppingListBtn.setOnClickListener {
            // TEMPORARY
            shoppingListsViewModel.addShoppingList("test")
        }
    }
}