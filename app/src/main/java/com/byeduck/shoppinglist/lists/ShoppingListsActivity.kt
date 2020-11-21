package com.byeduck.shoppinglist.lists

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.byeduck.shoppinglist.MainActivity
import com.byeduck.shoppinglist.R
import com.byeduck.shoppinglist.databinding.ActivityShoppingListsBinding
import com.byeduck.shoppinglist.util.PREF_FILE_NAME

class ShoppingListsActivity : AppCompatActivity() {

    private lateinit var viewModel: ShoppingListsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityShoppingListsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
        ).get(ShoppingListsViewModel::class.java)
        val listColor =
            getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE).getInt("listColor", R.color.white)
        binding.shoppingListsRecycleView.adapter =
            ShoppingListsAdapter(this, viewModel, supportFragmentManager, getColor(listColor))
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
            val intent = Intent(applicationContext, AddEditShoppingListActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        val backToMainIntent = Intent(this, MainActivity::class.java)
        startActivity(backToMainIntent)
    }
}