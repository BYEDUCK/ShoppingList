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
import com.byeduck.shoppinglist.model.view.ShoppingListWithElements

class ShoppingListsAdapter(
    private val context: Context,
    private val viewModel: ShoppingListsViewModel,
    private val fragmentManager: FragmentManager
) :
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
        holder.binding.root.setOnLongClickListener {
            val dialog = ShoppingActionsDialogFragment { action ->
                when (action) {
                    Action.DELETE -> viewModel.deleteShoppingList(shoppingLists[position])
                    Action.EDIT -> TODO()
                }
            }
            dialog.show(fragmentManager, "dialog")
            true
        }
    }

    override fun getItemCount(): Int = shoppingLists.size

    internal fun setShoppingLists(shoppingLists: List<ShoppingListWithElements>) {
        this.shoppingLists = shoppingLists.sortedBy { it.updatedAt }
        notifyDataSetChanged()
    }

}