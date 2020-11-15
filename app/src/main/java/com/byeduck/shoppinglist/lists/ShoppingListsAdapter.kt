package com.byeduck.shoppinglist.lists

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.byeduck.shoppinglist.R
import com.byeduck.shoppinglist.model.view.ShoppingListWithElements

class ShoppingListsAdapter(val context: Context, val viewModel: ShoppingListsViewModel) :
    RecyclerView.Adapter<ShoppingListsAdapter.ShoppingListViewHolder>() {

    private var shoppingLists: List<ShoppingListWithElements> = emptyList()

    inner class ShoppingListViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.shoppingListNameLbl)
        val createdAtTextView: TextView = view.findViewById(R.id.shoppingListCreatedAtLbl)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.listelem_shopping_list, parent, false)
        return ShoppingListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShoppingListViewHolder, position: Int) {
        val current = shoppingLists[position]
        holder.nameTextView.text = current.listName
        holder.createdAtTextView.text = current.createdAt.toString()

        holder.view.setOnClickListener {
            //TODO: create intent and go to list detail activity
        }
    }

    override fun getItemCount(): Int = shoppingLists.size

    internal fun setShoppingLists(shoppingLists: List<ShoppingListWithElements>) {
        this.shoppingLists = shoppingLists.sortedBy { it.updatedAt }
        notifyDataSetChanged()
    }

}