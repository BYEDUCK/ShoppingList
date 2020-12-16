package com.byeduck.shoppinglist.lists

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.byeduck.shoppinglist.databinding.ActivityAddEditShoppingListBinding
import com.byeduck.shoppinglist.model.view.ShoppingList
import com.google.gson.Gson

class AddEditShoppingListActivity : AppCompatActivity() {

    private lateinit var viewModel: ShoppingListsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAddEditShoppingListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
        ).get(ShoppingListsViewModel::class.java)
        val shoppingListJson = intent?.extras?.getString("serializedList", "")
        if (shoppingListJson.isNullOrEmpty()) {
            binding.shoppingListEditSaveBtn.setOnClickListener {
                val name = binding.shoppingListNameEditTxt.text.toString()
                viewModel.addShoppingList(name)
                val intent = Intent(applicationContext, ShoppingListsActivity::class.java)
                startActivity(intent)
            }
        } else {
            val shoppingList = Gson().fromJson(shoppingListJson, ShoppingList::class.java)
            binding.shoppingListNameEditTxt.setText(shoppingList.name)
            binding.shoppingListEditSaveBtn.setOnClickListener {
                shoppingList.name = binding.shoppingListNameEditTxt.text.toString()
                viewModel.updateShoppingList(shoppingList)
                val intent = Intent(applicationContext, ShoppingListsActivity::class.java)
                startActivity(intent)
            }
        }
    }
}