package com.byeduck.shoppinglist.detail

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.byeduck.shoppinglist.action.Action
import com.byeduck.shoppinglist.action.ShoppingActionsDialogFragment
import com.byeduck.shoppinglist.common.FirebaseRecyclerViewAdapterBase
import com.byeduck.shoppinglist.common.ShoppingListConverter
import com.byeduck.shoppinglist.common.ShoppingListsViewModel
import com.byeduck.shoppinglist.databinding.ListelemShoppingElemBinding
import com.byeduck.shoppinglist.model.ShoppingElementModel
import com.byeduck.shoppinglist.model.view.ShoppingElement
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.gson.Gson

class ShoppingElementsAdapter(
    private val context: Context,
    private val viewModel: ShoppingListsViewModel,
    private val fragmentManager: FragmentManager,
    private val elemColor: Int,
    private val elemTxtColor: Int,
    private val listId: String
) : FirebaseRecyclerViewAdapterBase<ShoppingElementModel, ShoppingElement, ShoppingElementsAdapter.ShoppingElementsViewHolder>(
    { s1, s2 -> s1.id.compareTo(s2.id) }, ShoppingElementModel::class.java
) {

    inner class ShoppingElementsViewHolder(val binding: ListelemShoppingElemBinding) :
        RecyclerView.ViewHolder(binding.root)

    init {
        val dbRef = viewModel.getDbListElemRef(listId)
        dbRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                childAdded(snapshot)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                childChanged(snapshot)
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                childRemoved(snapshot)
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ShoppingElemAdapter", error.message, error.toException())
            }

        })
    }

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

    override fun getItemCount(): Int = itemsToView.size

    override fun viewItemFromModel(model: ShoppingElementModel): ShoppingElement {
        return ShoppingListConverter.elemFromModel(model)
    }
}