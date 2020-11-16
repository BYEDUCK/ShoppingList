package com.byeduck.shoppinglist.lists

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.byeduck.shoppinglist.databinding.ListelemShoppingListBinding
import com.byeduck.shoppinglist.detail.ShoppingListDetailActivity
import com.byeduck.shoppinglist.model.view.ShoppingListWithElements

class ShoppingListsAdapter(private val context: Context) :
    RecyclerView.Adapter<ShoppingListsAdapter.ShoppingListViewHolder>() {

    private var shoppingLists: List<ShoppingListWithElements> = emptyList()

    inner class ShoppingListViewHolder(val binding: ListelemShoppingListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListelemShoppingListBinding.inflate(inflater, parent, false)
        return ShoppingListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShoppingListViewHolder, position: Int) {
        val current = shoppingLists[position]
        holder.binding.shoppingListNameLbl.text = current.listName
        holder.binding.shoppingListCreatedAtLbl.text = current.createdAt.toString()

        holder.binding.root.setOnClickListener {
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