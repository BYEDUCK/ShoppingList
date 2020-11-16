package com.byeduck.shoppinglist.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.byeduck.shoppinglist.R
import com.byeduck.shoppinglist.model.view.ShoppingElement

class ShoppingListElementsAdapter(private val context: Context) :
    RecyclerView.Adapter<ShoppingListElementsAdapter.ShoppingListElementsViewHolder>() {

    private var shoppingElements: List<ShoppingElement> = emptyList()

    inner class ShoppingListElementsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val elementTextLbl: TextView = view.findViewById(R.id.listElemTextLbl)
        val elementCountLbl: TextView = view.findViewById(R.id.listElemCountLbl)
        val elementPriceLbl: TextView = view.findViewById(R.id.listElemPriceLbl)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShoppingListElementsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.listelem_shopping_elem, parent, false)
        return ShoppingListElementsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShoppingListElementsViewHolder, position: Int) {
        val current = shoppingElements[position]
        holder.elementTextLbl.text = current.text
        holder.elementCountLbl.text = current.count.toString()
        holder.elementPriceLbl.text = current.price.toString()
    }

    override fun getItemCount(): Int = shoppingElements.size

    internal fun setShoppingElements(shoppingElements: List<ShoppingElement>) {
        this.shoppingElements = shoppingElements
        notifyDataSetChanged()
    }
}