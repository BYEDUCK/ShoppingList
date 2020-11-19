package com.byeduck.shoppinglist.detail

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.byeduck.shoppinglist.databinding.ActivityAddEditShoppingElementBinding
import com.byeduck.shoppinglist.model.view.ShoppingElement
import com.google.gson.Gson

class AddEditShoppingElementActivity : AppCompatActivity() {

    private lateinit var viewModel: ShoppingListsDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAddEditShoppingElementBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val shoppingElemJson = intent?.extras?.getString("serializedElem", "")
        val listId = intent.extras?.getLong("listId", -1L) ?: -1L
        viewModel = ViewModelProvider(
            this, ShoppingListsDetailViewModelFactory(this.application, listId)
        ).get(ShoppingListsDetailViewModel::class.java)
        val completedIntent = Intent(this, ShoppingListDetailActivity::class.java)
        if (shoppingElemJson.isNullOrEmpty()) {
            binding.shoppingElemCountEdit.setText("1")
            binding.shoppingElemPriceEdit.setText("0.0")
            binding.shoppingElemEditSaveBtn.setOnClickListener {
                viewModel.addShoppingElement(
                    binding.shoppingElemTxtEdit.text.toString(),
                    binding.shoppingElemPriceEdit.text.toString().toDouble(),
                    binding.shoppingElemCountEdit.text.toString().toInt()
                )
                completedIntent.putExtra("listId", listId)
                startActivity(completedIntent)
            }
        } else {
            val shoppingElement: ShoppingElement =
                Gson().fromJson(shoppingElemJson, ShoppingElement::class.java)
            binding.shoppingElemTxtEdit.setText(shoppingElement.text)
            binding.shoppingElemCountEdit.setText(shoppingElement.count.toString())
            binding.shoppingElemPriceEdit.setText(shoppingElement.price.toString())
            binding.shoppingElemEditSaveBtn.setOnClickListener {
                viewModel.updateShoppingElement(
                    ShoppingElement(
                        shoppingElement.id, shoppingElement.listId,
                        binding.shoppingElemTxtEdit.text.toString(),
                        binding.shoppingElemPriceEdit.text.toString().toDouble(),
                        binding.shoppingElemCountEdit.text.toString().toInt(),
                        shoppingElement.isChecked
                    )
                )
                completedIntent.putExtra("listId", shoppingElement.listId)
                startActivity(completedIntent)
            }
        }
    }
}