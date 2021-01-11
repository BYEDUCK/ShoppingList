package com.byeduck.shoppinglist.detail

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.byeduck.shoppinglist.action.Action
import com.byeduck.shoppinglist.action.ShoppingActionsDialogFragment
import com.byeduck.shoppinglist.common.FirebaseRecyclerViewAdapterBase
import com.byeduck.shoppinglist.common.converter.ShoppingListConverter
import com.byeduck.shoppinglist.common.viewmodel.ShoppingListsViewModel
import com.byeduck.shoppinglist.databinding.ListelemShoppingElemBinding
import com.byeduck.shoppinglist.model.ShoppingElementModel
import com.byeduck.shoppinglist.model.view.ShoppingElement
import com.google.gson.Gson

class ShoppingElementsAdapter(
    private val context: Context,
    private val viewModel: ShoppingListsViewModel,
    private val fragmentManager: FragmentManager,
    private val elemColor: Int,
    private val elemTxtColor: Int,
    private val listId: String
) : FirebaseRecyclerViewAdapterBase<ShoppingElementModel, ShoppingElement, ShoppingElementsAdapter.ShoppingElementsViewHolder>(
    viewModel.getDbListElemRef(listId),
    { s1, s2 -> s1.id.compareTo(s2.id) },
    ShoppingElementModel::class.java
) {

    inner class ShoppingElementsViewHolder(val binding: ListelemShoppingElemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShoppingElementsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListelemShoppingElemBinding.inflate(inflater, parent, false)
        binding.root.setBackgroundColor(elemColor)
        binding.listElemPriceLbl.setTextColor(elemTxtColor)
        binding.listElemCountLbl.setTextColor(elemTxtColor)
        binding.listElemTextLbl.setTextColor(elemTxtColor)
        return ShoppingElementsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShoppingElementsViewHolder, position: Int) {
        val current = itemsToView[position]
        holder.binding.listElemTextLbl.text = current.text
        holder.binding.listElemCountLbl.text = current.count.toString()
        holder.binding.listElemPriceLbl.text = current.price.toString()
        holder.binding.listElemCheck.isChecked = current.isChecked
        holder.binding.listElemCheck.setOnClickListener {
            val checkBox = it as CheckBox
            current.isChecked = checkBox.isChecked
            viewModel.updateShoppingElem(listId, current)
        }
        holder.binding.root.setOnLongClickListener {
            val dialog = ShoppingActionsDialogFragment { action ->
                when (action) {
                    Action.DELETE -> viewModel.deleteElemById(listId, current.id)
                        .addOnCanceledListener {
                            Toast.makeText(
                                context,
                                "Failed to delete elem ${current.id}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    Action.EDIT -> {
                        val intent = Intent(context, AddEditShoppingElementActivity::class.java)
                        intent.putExtra("serializedElem", Gson().toJson(current))
                        context.startActivity(intent)
                    }
                }
            }
            dialog.show(fragmentManager, "dialog")
            true
        }
    }

    override fun viewItemFromModel(model: ShoppingElementModel): ShoppingElement {
        return ShoppingListConverter.elemFromModel(model)
    }
}