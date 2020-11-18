package com.byeduck.shoppinglist.lists

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.byeduck.shoppinglist.databinding.ActivityShoppingListsBinding

class ShoppingListsActivity : AppCompatActivity() {

    private lateinit var viewModel: ShoppingListsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityShoppingListsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
        ).get(ShoppingListsViewModel::class.java)
        binding.shoppingListsRecycleView.adapter =
            ShoppingListsAdapter(this, viewModel, supportFragmentManager)
        binding.shoppingListsRecycleView.layoutManager = LinearLayoutManager(this)
        binding.shoppingListsRecycleView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        viewModel.shoppingLists.observe(this, { all ->
            all?.let {
                (binding.shoppingListsRecycleView.adapter as ShoppingListsAdapter).setShoppingLists(
                    it
                )
            }
        })
        binding.addShoppingListBtn.setOnClickListener {
            // TEMPORARY
            viewModel.addShoppingList("test")
        }
    }
}