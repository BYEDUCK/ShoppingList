package com.byeduck.shoppinglist.lists

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.byeduck.shoppinglist.R

class ShoppingListsActivity : AppCompatActivity() {

    private lateinit var shoppingListsViewModel: ShoppingListsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_lists)
        val recycleView = this.findViewById<RecyclerView>(R.id.shoppingListsRecycleView)
        shoppingListsViewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
        ).get(ShoppingListsViewModel::class.java)
        recycleView.adapter = ShoppingListsAdapter(this, shoppingListsViewModel)
        recycleView.layoutManager = LinearLayoutManager(this)
        recycleView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL))
        shoppingListsViewModel.shoppingLists.observe(this, Observer { all ->
            all?.let { (recycleView.adapter as ShoppingListsAdapter).setShoppingLists(it) }
        })
    }
}