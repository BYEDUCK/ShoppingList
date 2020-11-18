package com.byeduck.shoppinglist.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.byeduck.shoppinglist.databinding.ActivityShoppingListDetailBinding

class ShoppingListDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: ShoppingListsDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityShoppingListDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val listId = intent.extras?.get("listId") as Long
        viewModel = ViewModelProvider(
            this, ShoppingListsDetailViewModelFactory(this.application, listId)
        ).get(ShoppingListsDetailViewModel::class.java)
        val adapter = ShoppingListElementsAdapter(viewModel, supportFragmentManager)
        binding.shoppingListElementsRecycleView.adapter = adapter
        binding.shoppingListElementsRecycleView.layoutManager = LinearLayoutManager(this)
        binding.shoppingListElementsRecycleView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        viewModel.shoppingList.observe(this, { list ->
            list?.let {
                binding.shoppingListNameLbl.text = it.listName
                (binding.shoppingListElementsRecycleView.adapter as ShoppingListElementsAdapter)
                    .setShoppingElements(it.elements)
            }
        })
        binding.addShoppingElementBtn.setOnClickListener {
            viewModel.addShoppingElement("Test element", 2.5)
        }
    }
}