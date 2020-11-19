package com.byeduck.shoppinglist.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.byeduck.shoppinglist.action.Action
import com.byeduck.shoppinglist.action.ShoppingActionsDialogFragment
import com.byeduck.shoppinglist.databinding.ListelemShoppingElemBinding
import com.byeduck.shoppinglist.model.view.ShoppingElement

class ShoppingListElementsAdapter(
    private val viewModel: ShoppingListsDetailViewModel,
    private val fragmentManager: FragmentManager
) : RecyclerView.Adapter<ShoppingListElementsAdapter.ShoppingListElementsViewHolder>() {

    private var shoppingElements: List<ShoppingElement> = emptyList()

    inner class ShoppingListElementsViewHolder(val binding: ListelemShoppingElemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShoppingListElementsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListelemShoppingElemBinding.inflate(inflater, parent, false)
        return ShoppingListElementsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShoppingListElementsViewHolder, position: Int) {
        val current = shoppingElements[position]
        holder.binding.listElemTextLbl.text = current.text
        holder.binding.listElemCountLbl.text = current.count.toString()
        holder.binding.listElemPriceLbl.text = current.price.toString()
        holder.binding.listElemCheck.isChecked = current.isChecked
        holder.binding.listElemCheck.setOnClickListener {
            val checkBox = it as CheckBox
            viewModel.checkShoppingElementById(checkBox.isChecked, current.id)
        }
        holder.binding.root.setOnLongClickListener {
            val dialog = ShoppingActionsDialogFragment { action ->
                when (action) {
                    Action.DELETE -> viewModel.deleteShoppingElement(current)
                    Action.EDIT -> TODO()
                }
            }
            dialog.show(fragmentManager, "dialog")
            true
        }
    }

    override fun getItemCount(): Int = shoppingElements.size

    internal fun setShoppingElements(shoppingElements: List<ShoppingElement>) {
        this.shoppingElements = shoppingElements
        notifyDataSetChanged()
    }
}