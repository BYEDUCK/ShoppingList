package com.byeduck.shoppinglist.lists

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.byeduck.shoppinglist.MainActivity
import com.byeduck.shoppinglist.R
import com.byeduck.shoppinglist.common.PREF_FILE_NAME
import com.byeduck.shoppinglist.common.viewmodel.ShoppingListsViewModel
import com.byeduck.shoppinglist.databinding.ActivityShoppingListsBinding

class ShoppingListsActivity : AppCompatActivity() {

    private lateinit var viewModel: ShoppingListsViewModel
    private lateinit var binding: ActivityShoppingListsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShoppingListsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
        ).get(ShoppingListsViewModel::class.java)
        val sharedPreferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE)
        val listColor = sharedPreferences.getInt("listColor", R.color.white)
        val listTxtColor = sharedPreferences.getInt("listTxtColor", R.color.black)
        binding.shoppingListsRecycleView.adapter =
            ShoppingListsAdapter(
                this, supportFragmentManager, getColor(listColor), getColor(listTxtColor), viewModel
            )
        binding.shoppingListsRecycleView.layoutManager = LinearLayoutManager(this)
        binding.shoppingListsRecycleView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        binding.addShoppingListBtn.setOnClickListener {
            val intent = Intent(applicationContext, AddEditShoppingListActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        val backToMainIntent = Intent(this, MainActivity::class.java)
        startActivity(backToMainIntent)
    }

    override fun onDestroy() {
        val adapter = binding.shoppingListsRecycleView.adapter as ShoppingListsAdapter
        adapter.removeListener()
        super.onDestroy()
    }
}