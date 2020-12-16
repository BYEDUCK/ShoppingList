package com.byeduck.shoppinglist.lists

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.byeduck.shoppinglist.action.Action
import com.byeduck.shoppinglist.action.ShoppingActionsDialogFragment
import com.byeduck.shoppinglist.common.FirebaseRecyclerViewAdapterBase
import com.byeduck.shoppinglist.common.ShoppingListConverter
import com.byeduck.shoppinglist.common.ShoppingListsViewModel
import com.byeduck.shoppinglist.databinding.ListelemShoppingListBinding
import com.byeduck.shoppinglist.detail.ShoppingListDetailActivity
import com.byeduck.shoppinglist.model.ShoppingListModel
import com.byeduck.shoppinglist.model.view.ShoppingList
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.gson.Gson

class ShoppingListsAdapter(
    private val context: Context,
    private val fragmentManager: FragmentManager,
    private val listColor: Int,
    private val listTxtColor: Int,
    private val viewModel: ShoppingListsViewModel
) :
    FirebaseRecyclerViewAdapterBase<ShoppingListModel, ShoppingList, ShoppingListsAdapter.ShoppingListViewHolder>(
        { s1, s2 ->
            if (s1.id == s2.id) 0
            else (s2.updatedAt.time - s1.updatedAt.time).toInt()
        }, ShoppingListModel::class.java
    ) {

    inner class ShoppingListViewHolder(val binding: ListelemShoppingListBinding) :
        RecyclerView.ViewHolder(binding.root)

    init {
        val dbRef = viewModel.getDbListsRef()
        dbRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previous: String?) {
                childAdded(snapshot)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previous: String?) {
                childChanged(snapshot)
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                childRemoved(snapshot)
            }

            override fun onChildMoved(snapshot: DataSnapshot, previous: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ShoppingListsAdapter", error.message, error.toException())
            }

        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListelemShoppingListBinding.inflate(inflater, parent, false)
        binding.root.setBackgroundColor(listColor)
        binding.shoppingListCreatedAtLbl.setTextColor(listTxtColor)
        binding.shoppingListNameLbl.setTextColor(listTxtColor)
        return ShoppingListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShoppingListViewHolder, position: Int) {
        val current = itemsToView[position]
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
                        .addOnCanceledListener {
                            Toast.makeText(
                                context,
                                "Failed to delete list ${current.id}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    Action.EDIT -> {
                        val intent = Intent(context, AddEditShoppingListActivity::class.java)
                        intent.putExtra("serializedList", Gson().toJson(current))
                        context.startActivity(intent)
                    }
                }
            }
            dialog.show(fragmentManager, "dialog")
            true
        }
    }

    override fun getItemCount(): Int = itemsToView.size

    override fun viewItemFromModel(model: ShoppingListModel): ShoppingList {
        return ShoppingListConverter.listFromModel(model)
    }

}