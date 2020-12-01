package com.byeduck.shoppinglist.detail

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.widget.NumberPicker
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.byeduck.shoppinglist.R
import com.byeduck.shoppinglist.databinding.ActivityAddEditShoppingElementBinding
import com.byeduck.shoppinglist.model.view.ShoppingElement
import com.google.gson.Gson

class AddEditShoppingElementActivity : AppCompatActivity() {

    private val pickerValues = generateNumberPickerDisplayValues()

    private lateinit var viewModel: ShoppingListsDetailViewModel
    private lateinit var activityBinding: ActivityAddEditShoppingElementBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityAddEditShoppingElementBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        val shoppingElemJson = intent?.extras?.getString("serializedElem", "")
        val listId = intent.extras?.getLong("listId", -1L) ?: -1L
        viewModel = ViewModelProvider(
            this, ShoppingListsDetailViewModelFactory(this.application, listId)
        ).get(ShoppingListsDetailViewModel::class.java)
        val completedIntent = Intent(this, ShoppingListDetailActivity::class.java)
        initNumberPicker(activityBinding.shoppingElemCountEdit)
        val elemId = intent?.extras?.getLong("elemId", -1L) ?: -1L
        if (shoppingElemJson.isNullOrEmpty() && elemId < 0L) {
            initEditFields()
            activityBinding.shoppingElemEditSaveBtn.setOnClickListener {
                val addedElemId = viewModel.addShoppingElement(
                    activityBinding.shoppingElemTxtEdit.text.toString(),
                    activityBinding.shoppingElemPriceEdit.text.toString().toDouble(),
                    pickerValues[activityBinding.shoppingElemCountEdit.value]
                )
                completedIntent.putExtra("listId", listId)
                sendBroadcastElemAdded(
                    listId,
                    addedElemId,
                    activityBinding.shoppingElemTxtEdit.text.toString()
                )
                startActivity(completedIntent)
            }
        } else {
            val shoppingElement: ShoppingElement =
                if (shoppingElemJson != null && shoppingElemJson.isNotBlank()) {
                    Gson().fromJson(shoppingElemJson, ShoppingElement::class.java)
                } else {
                    viewModel.getShoppingElementById(elemId)
                }
            initEditFields(shoppingElement.text, shoppingElement.price, shoppingElement.count)
            activityBinding.shoppingElemEditSaveBtn.setOnClickListener {
                viewModel.updateShoppingElement(
                    ShoppingElement(
                        shoppingElement.id, shoppingElement.listId,
                        activityBinding.shoppingElemTxtEdit.text.toString(),
                        activityBinding.shoppingElemPriceEdit.text.toString().toDouble(),
                        pickerValues[activityBinding.shoppingElemCountEdit.value],
                        shoppingElement.isChecked
                    )
                )
                completedIntent.putExtra("listId", shoppingElement.listId)
                startActivity(completedIntent)
            }
        }
    }

    private fun initNumberPicker(numberPicker: NumberPicker) {
        numberPicker.apply {
            minValue = 0
            maxValue = 99
            displayedValues = pickerValues.map { it.toString() }.toTypedArray()
        }
    }

    private fun initEditFields(elemTxt: String = "", elemPrice: Double = 0.0, elemCount: Int = 1) {
        activityBinding.shoppingElemCountEdit.value = elemCount - 1
        activityBinding.shoppingElemTxtEdit.setText(elemTxt)
        activityBinding.shoppingElemPriceEdit.setText(elemPrice.toString())
    }

    private fun sendBroadcastElemAdded(listId: Long, elemId: Long, elemTxt: String) {
        val broadcastIntent = Intent("${getString(R.string.mainPackage)}.NOTI").apply {
            putExtra("listId", listId)
            putExtra("elemTxt", elemTxt)
            putExtra("elemId", elemId)
            component = ComponentName(
                getString(R.string.notificationPackage),
                "${getString(R.string.notificationPackage)}.${getString(R.string.broadcastReceiverClassName)}"
            )
            addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
        }
        sendBroadcast(
            broadcastIntent,
            "${getString(R.string.mainPackage)}.permissions.RECEIVE_NOTI"
        )
    }

    private fun generateNumberPickerDisplayValues(): IntArray {
        val res = IntArray(100)
        for (i in 0 until 100) {
            res[i] = i + 1
        }
        return res
    }
}