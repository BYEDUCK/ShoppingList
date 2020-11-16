package com.byeduck.shoppinglist.lists

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.byeduck.shoppinglist.R
import com.byeduck.shoppinglist.detail.ShoppingListDetailActivity
import com.byeduck.shoppinglist.model.view.ShoppingListWithElements

class ShoppingListsAdapter(private val context: Context) :
    RecyclerView.Adapter<ShoppingListsAdapter.ShoppingListViewHolder>() {

    private var shoppingLists: List<ShoppingListWithElements> = emptyList()

    inner class ShoppingListViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val shoppingListNameLbl: TextView = view.findViewById(R.id.shoppingListNameLbl)
        val shoppingListCreatedAtLbl: TextView = view.findViewById(R.id.shoppingListCreatedAtLbl)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.listelem_shopping_list, parent, false)
        return ShoppingListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShoppingListViewHolder, position: Int) {
        val current = shoppingLists[position]
        holder.shoppingListNameLbl.text = current.listName
        holder.shoppingListCreatedAtLbl.text = current.createdAt.toString()

        holder.view.setOnClickListener {
            val intent = Intent(context, ShoppingListDetailActivity::class.java)
            intent.putExtra("listId", current.listId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = shoppingLists.size

    internal fun setShoppingLists(shoppingLists: List<ShoppingListWithElements>) {
        this.shoppingLists = shoppingLists.sortedBy { it.updatedAt }
        notifyDataSetChanged()
    }

}