package com.byeduck.shoppinglist.detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
        val pickerValues = generateNumberPickerDisplayValues()
        binding.shoppingElemCountEdit.minValue = 0
        binding.shoppingElemCountEdit.maxValue = 99
        binding.shoppingElemCountEdit.displayedValues =
            pickerValues.map { it.toString() }.toTypedArray()
        if (shoppingElemJson.isNullOrEmpty()) {
            binding.shoppingElemCountEdit.value = 0
            binding.shoppingElemPriceEdit.setText("0.0")
            binding.shoppingElemEditSaveBtn.setOnClickListener {
                viewModel.addShoppingElement(
                    binding.shoppingElemTxtEdit.text.toString(),
                    binding.shoppingElemPriceEdit.text.toString().toDouble(),
                    pickerValues[binding.shoppingElemCountEdit.value]
                )
                completedIntent.putExtra("listId", listId)
                val broadcastIntent = Intent("com.byeduck.shoppinglist.NOTI")
                broadcastIntent.putExtra("listId", listId)
                sendBroadcast(
                    broadcastIntent,
                    "com.byeduck.shoppinglist.permissions.SEND_RECEIVE_NOTI"
                )
                Log.v("Add/Edit Activity", "Broadcast sent. List id : $listId")
                startActivity(completedIntent)
            }
        } else {
            val shoppingElement: ShoppingElement =
                Gson().fromJson(shoppingElemJson, ShoppingElement::class.java)
            binding.shoppingElemTxtEdit.setText(shoppingElement.text)
            binding.shoppingElemCountEdit.value = shoppingElement.count - 1
            binding.shoppingElemPriceEdit.setText(shoppingElement.price.toString())
            binding.shoppingElemEditSaveBtn.setOnClickListener {
                viewModel.updateShoppingElement(
                    ShoppingElement(
                        shoppingElement.id, shoppingElement.listId,
                        binding.shoppingElemTxtEdit.text.toString(),
                        binding.shoppingElemPriceEdit.text.toString().toDouble(),
                        pickerValues[binding.shoppingElemCountEdit.value],
                        shoppingElement.isChecked
                    )
                )
                completedIntent.putExtra("listId", shoppingElement.listId)
                startActivity(completedIntent)
            }
        }
    }

    private fun generateNumberPickerDisplayValues(): IntArray {
        val res = IntArray(100)
        for (i in 0 until 100) {
            res[i] = i + 1
        }
        return res
    }
}