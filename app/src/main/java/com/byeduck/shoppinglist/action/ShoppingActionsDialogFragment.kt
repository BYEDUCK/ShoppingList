package com.byeduck.shoppinglist.action

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class ShoppingActionsDialogFragment(private val onActionCallback: (action: Action) -> Unit) :
    DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { fragmentActivity ->
            val builder = AlertDialog.Builder(fragmentActivity)
            val elemActions = Action.values()
            builder
                .setTitle("Pick action")
                .setItems(elemActions.map { it.toString() }.toTypedArray()) { _, which ->
                    onActionCallback(elemActions[which])
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}