package com.byeduck.shoppinglist.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.byeduck.shoppinglist.databinding.ListelemShoppingElemBinding
import com.byeduck.shoppinglist.model.view.ShoppingElement

class ShoppingListElementsAdapter :
    RecyclerView.Adapter<ShoppingListElementsAdapter.ShoppingListElementsViewHolder>() {

    private var shoppingElements: List<ShoppingElement> = emptyList()

    inner class ShoppingListElementsViewHolder(val binding: ListelemShoppingElemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShoppingListElementsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ShoppingListElementsViewHolder(ListelemShoppingElemBinding.inflate(inflater))
    }

    override fun onBindViewHolder(holder: ShoppingListElementsViewHolder, position: Int) {
        val current = shoppingElements[position]
        holder.binding.listElemTextLbl.text = current.text
        holder.binding.listElemCountLbl.text = current.count.toString()
        holder.binding.listElemPriceLbl.text = current.price.toString()
    }

    override fun getItemCount(): Int = shoppingElements.size

    internal fun setShoppingElements(shoppingElements: List<ShoppingElement>) {
        this.shoppingElements = shoppingElements
        notifyDataSetChanged()
    }
}