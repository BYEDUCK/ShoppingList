package com.byeduck.shoppinglist.lists.edit

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.byeduck.shoppinglist.databinding.ActivityEditShoppingListBinding
import com.byeduck.shoppinglist.lists.ShoppingListsActivity
import com.byeduck.shoppinglist.lists.ShoppingListsViewModel
import com.byeduck.shoppinglist.model.view.ShoppingList
import com.google.gson.Gson

class EditShoppingListActivity : AppCompatActivity() {

    private lateinit var viewModel: ShoppingListsViewModel
    private lateinit var shoppingList: ShoppingList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityEditShoppingListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
        ).get(ShoppingListsViewModel::class.java)
        val shoppingListJson = intent?.extras?.getString("serializedList", "")
        shoppingList = Gson().fromJson(shoppingListJson, ShoppingList::class.java)
        binding.shoppingListNameEditTxt.setText(shoppingList.name)
        binding.shoppingListEditSaveBtn.setOnClickListener {
            shoppingList.name = binding.shoppingListNameEditTxt.text.toString()
            viewModel.editShoppingList(shoppingList)
            val intent = Intent(applicationContext, ShoppingListsActivity::class.java)
            startActivity(intent)
        }
    }
}