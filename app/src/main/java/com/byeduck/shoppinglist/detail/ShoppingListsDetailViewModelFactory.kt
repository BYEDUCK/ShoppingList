package com.byeduck.shoppinglist.detail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ShoppingListsDetailViewModelFactory(
    private val application: Application,
    private val listId: Long = -1L
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ShoppingListsDetailViewModel(application, listId) as T
    }
}