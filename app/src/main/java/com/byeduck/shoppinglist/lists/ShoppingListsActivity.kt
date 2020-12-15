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
}