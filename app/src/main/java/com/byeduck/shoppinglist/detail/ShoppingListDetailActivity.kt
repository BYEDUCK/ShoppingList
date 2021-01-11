package com.byeduck.shoppinglist.detail

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.byeduck.shoppinglist.R
import com.byeduck.shoppinglist.common.PREF_FILE_NAME
import com.byeduck.shoppinglist.common.viewmodel.ShoppingListsViewModel
import com.byeduck.shoppinglist.databinding.ActivityShoppingListDetailBinding
import com.byeduck.shoppinglist.lists.ShoppingListsActivity

class ShoppingListDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: ShoppingListsViewModel
    private lateinit var binding: ActivityShoppingListDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShoppingListDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val listId = intent.extras?.getString("listId", "") ?: ""
        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
        ).get(ShoppingListsViewModel::class.java)
        val sharedPreferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE)
        val elemColor = sharedPreferences.getInt("elemColor", R.color.white)
        val elemTxtColor = sharedPreferences.getInt("elemTxtColor", R.color.black)
        val adapter = ShoppingElementsAdapter(
            this,
            viewModel,
            supportFragmentManager,
            getColor(elemColor), getColor(elemTxtColor),
            listId
        )
        binding.shoppingListElementsRecycleView.adapter = adapter
        binding.shoppingListElementsRecycleView.layoutManager = LinearLayoutManager(this)
        binding.shoppingListElementsRecycleView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
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

    override fun onDestroy() {
        val adapter = binding.shoppingListElementsRecycleView.adapter as ShoppingElementsAdapter
        adapter.removeListener()
        super.onDestroy()
    }
}