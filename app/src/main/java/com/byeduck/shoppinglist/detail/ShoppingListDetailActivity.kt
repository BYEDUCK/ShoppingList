package com.byeduck.shoppinglist.detail

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.byeduck.shoppinglist.R
import com.byeduck.shoppinglist.databinding.ActivityShoppingListDetailBinding
import com.byeduck.shoppinglist.lists.ShoppingListsActivity
import com.byeduck.shoppinglist.util.PREF_FILE_NAME

class ShoppingListDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: ShoppingListsDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityShoppingListDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val listId = intent.extras?.getLong("listId", -1L) ?: -1L
        viewModel = ViewModelProvider(
            this, ShoppingListsDetailViewModelFactory(this.application, listId)
        ).get(ShoppingListsDetailViewModel::class.java)
        val sharedPreferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE)
        val elemColor = sharedPreferences.getInt("elemColor", R.color.white)
        val elemTxtColor = sharedPreferences.getInt("elemTxtColor", R.color.black)
        val adapter = ShoppingListElementsAdapter(
            this,
            viewModel,
            supportFragmentManager,
            getColor(elemColor), getColor(elemTxtColor)
        )
        binding.shoppingListElementsRecycleView.adapter = adapter
        binding.shoppingListElementsRecycleView.layoutManager = LinearLayoutManager(this)
        binding.shoppingListElementsRecycleView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
//        viewModel.shoppingList.observe(this, { list ->
//            list?.let {
//                binding.shoppingListNameLbl.text = it.listName
//                (binding.shoppingListElementsRecycleView.adapter as ShoppingListElementsAdapter)
//                    .setShoppingElements(it.elements)
//            }
//        })
        binding.addShoppingElementBtn.setOnClickListener {
            val intent = Intent(applicationContext, AddEditShoppingElementActivity::class.java)
            intent.putExtra("listId", listId)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        val backIntent = Intent(this, ShoppingListsActivity::class.java)
        startActivity(backIntent)
    }
}