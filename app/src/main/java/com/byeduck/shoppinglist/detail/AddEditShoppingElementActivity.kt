package com.byeduck.shoppinglist.detail

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.NumberPicker
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.byeduck.shoppinglist.MainActivity
import com.byeduck.shoppinglist.R
import com.byeduck.shoppinglist.databinding.ActivityAddEditShoppingElementBinding
import com.byeduck.shoppinglist.model.ShoppingElementModel
import com.byeduck.shoppinglist.model.view.ShoppingElement
import com.byeduck.shoppinglist.util.ShoppingListConverter
import com.byeduck.shoppinglist.util.ShoppingListsViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddEditShoppingElementActivity : AppCompatActivity() {

    private val pickerValues = generateNumberPickerDisplayValues()

    private lateinit var viewModel: ShoppingListsViewModel
    private lateinit var activityBinding: ActivityAddEditShoppingElementBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityAddEditShoppingElementBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        val shoppingElemJson = intent?.extras?.getString("serializedElem", "")
        val listId = intent.extras?.getString("listId", "") ?: ""
        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
        ).get(ShoppingListsViewModel::class.java)
        val completedIntent = Intent(this, ShoppingListDetailActivity::class.java)
        initNumberPicker(activityBinding.shoppingElemCountEdit)
        val elemId = intent?.extras?.getString("elemId", "") ?: ""
        if (shoppingElemJson.isNullOrEmpty() && elemId.isBlank()) {
            initEditFields()
            activityBinding.shoppingElemEditSaveBtn.setOnClickListener {
                GlobalScope.launch {
                    val addedElemId = viewModel.addShoppingListElementAsync(
                        listId,
                        activityBinding.shoppingElemTxtEdit.text.toString(),
                        activityBinding.shoppingElemPriceEdit.text.toString().toDouble(),
                        pickerValues[activityBinding.shoppingElemCountEdit.value]
                    ).await()
                    completedIntent.putExtra("listId", listId)
                    sendBroadcastElemAdded(
                        listId,
                        addedElemId,
                        activityBinding.shoppingElemTxtEdit.text.toString()
                    )
                    startActivity(completedIntent)
                }
            }
        } else {
            activityBinding.shoppingElemEditSaveBtn.isEnabled = false
            if (shoppingElemJson != null && shoppingElemJson.isNotBlank()) {
                val elem = Gson().fromJson(shoppingElemJson, ShoppingElement::class.java)
                initEditUi(elem, listId)
            } else {
                viewModel.getDbListElemRef(listId)
                    .child(elemId)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val model = snapshot.getValue(ShoppingElementModel::class.java)
                            if (model == null) {
                                backToMain()
                            } else {
                                val elem = ShoppingListConverter.elemFromModel(model)
                                initEditUi(elem, listId)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.e("AddEditElemError", "Cannot read from db", error.toException())
                            backToMain()
                        }

                    })
            }
        }
    }

    private fun backToMain() {
        val backToMainIntent =
            Intent(applicationContext, MainActivity::class.java)
        startActivity(backToMainIntent)
    }

    private fun initEditUi(shoppingElement: ShoppingElement, listId: String) {
        val completedIntent = Intent(this, ShoppingListDetailActivity::class.java)
        activityBinding.shoppingElemEditSaveBtn.isEnabled = true
        initEditFields(shoppingElement.text, shoppingElement.price, shoppingElement.count)
        activityBinding.shoppingElemEditSaveBtn.setOnClickListener {
            viewModel.updateShoppingElem(
                listId,
                ShoppingElement(
                    shoppingElement.id,
                    activityBinding.shoppingElemTxtEdit.text.toString(),
                    activityBinding.shoppingElemPriceEdit.text.toString().toDouble(),
                    pickerValues[activityBinding.shoppingElemCountEdit.value],
                    shoppingElement.isChecked
                )
            )
            completedIntent.putExtra("listId", listId)
            startActivity(completedIntent)
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

    private fun sendBroadcastElemAdded(listId: String, elemId: String, elemTxt: String) {
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