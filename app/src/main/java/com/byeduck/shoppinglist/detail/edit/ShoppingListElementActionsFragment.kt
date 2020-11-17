package com.byeduck.shoppinglist.detail.edit

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class ShoppingListElementActionsFragment(
    private val deleteCallback: () -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val elemActions = arrayOf("EDIT", "DELETE")
            builder
                .setTitle("Pick action")
                .setItems(elemActions) { _, which ->
                    when (elemActions[which]) {
                        "DELETE" -> deleteCallback()
                    }
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }


}