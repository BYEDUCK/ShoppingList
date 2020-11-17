package com.byeduck.shoppinglist.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.byeduck.shoppinglist.databinding.ActivityShoppingListDetailBinding

class ShoppingListDetailActivity : AppCompatActivity() {

    private lateinit var shoppingListDetailViewModel: ShoppingListsDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityShoppingListDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val listId = intent.extras?.get("listId") as Long
        shoppingListDetailViewModel = ViewModelProvider(
            this, ShoppingListsDetailViewModelFactory(this.application, listId)
        ).get(ShoppingListsDetailViewModel::class.java)
        val adapter = ShoppingListElementsAdapter(supportFragmentManager) {
            shoppingListDetailViewModel.removeShoppingElement(it)
        }
        binding.shoppingListElementsRecycleView.adapter = adapter
        binding.shoppingListElementsRecycleView.layoutManager = LinearLayoutManager(this)
        binding.shoppingListElementsRecycleView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        shoppingListDetailViewModel.shoppingList.observe(this, Observer { list ->
            list?.let {
                binding.shoppingListNameLbl.text = it.listName
                (binding.shoppingListElementsRecycleView.adapter as ShoppingListElementsAdapter)
                    .setShoppingElements(it.elements)
            }
        })
        binding.addShoppingElementBtn.setOnClickListener {
            shoppingListDetailViewModel.addShoppingElement("Test element", 2.5)
        }
    }
}