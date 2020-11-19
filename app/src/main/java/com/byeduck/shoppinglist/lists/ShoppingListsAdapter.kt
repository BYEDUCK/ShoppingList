package com.byeduck.shoppinglist.lists

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.byeduck.shoppinglist.action.Action
import com.byeduck.shoppinglist.action.ShoppingActionsDialogFragment
import com.byeduck.shoppinglist.databinding.ListelemShoppingListBinding
import com.byeduck.shoppinglist.detail.ShoppingListDetailActivity
import com.byeduck.shoppinglist.lists.edit.EditShoppingListActivity
import com.byeduck.shoppinglist.model.view.ShoppingList
import com.google.gson.Gson

class ShoppingListsAdapter(
    private val context: Context,
    private val viewModel: ShoppingListsViewModel,
    private val fragmentManager: FragmentManager
) :
    RecyclerView.Adapter<ShoppingListsAdapter.ShoppingListViewHolder>() {

    private var shoppingLists: List<ShoppingList> = emptyList()

    inner class ShoppingListViewHolder(val binding: ListelemShoppingListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListelemShoppingListBinding.inflate(inflater, parent, false)
        return ShoppingListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShoppingListViewHolder, position: Int) {
        val current = shoppingLists[position]
        holder.binding.shoppingListNameLbl.text = current.name
        holder.binding.shoppingListCreatedAtLbl.text = current.updatedAt.toString()
        holder.binding.root.setOnClickListener {
            val intent = Intent(context, ShoppingListDetailActivity::class.java)
            intent.putExtra("listId", current.id)
            context.startActivity(intent)
        }
        holder.binding.root.setOnLongClickListener {
            val dialog = ShoppingActionsDialogFragment { action ->
                when (action) {
                    Action.DELETE -> viewModel.deleteShoppingListById(current.id)
                    Action.EDIT -> {
                        val intent = Intent(context, EditShoppingListActivity::class.java)
                        intent.putExtra("serializedList", Gson().toJson(current))
                        context.startActivity(intent)
                    }
                }
            }
            dialog.show(fragmentManager, "dialog")
            true
        }
    }

    override fun getItemCount(): Int = shoppingLists.size

    internal fun setShoppingLists(shoppingLists: List<ShoppingList>) {
        this.shoppingLists = shoppingLists.sortedByDescending { it.updatedAt }
        notifyDataSetChanged()
    }

}