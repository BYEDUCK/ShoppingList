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
import com.byeduck.shoppinglist.databinding.ListelemShoppingListBinding
import com.byeduck.shoppinglist.detail.ShoppingListDetailActivity
import com.byeduck.shoppinglist.model.ShoppingListModel
import com.byeduck.shoppinglist.model.view.ShoppingList
import com.byeduck.shoppinglist.util.ShoppingListConverter
import com.byeduck.shoppinglist.util.ShoppingListsViewModel
import com.google.firebase.database.*
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import java.util.concurrent.ConcurrentSkipListSet

class ShoppingListsAdapter(
    private val context: Context,
    private val fragmentManager: FragmentManager,
    private val listColor: Int,
    private val listTxtColor: Int,
    private val viewModel: ShoppingListsViewModel
) :
    RecyclerView.Adapter<ShoppingListsAdapter.ShoppingListViewHolder>() {

    private val idsRegistry = ConcurrentSkipListSet<String>()
    private var shoppingListsToView: List<ShoppingList> = emptyList()
    private val shoppingLists =
        ConcurrentSkipListSet<ShoppingList> { s1, s2 ->
            if (s1.id == s2.id) 0
            else (s2.updatedAt.time - s1.updatedAt.time).toInt()
        }

    inner class ShoppingListViewHolder(val binding: ListelemShoppingListBinding) :
        RecyclerView.ViewHolder(binding.root)

    init {
        val dbRef = viewModel.getDbListsRef()
        dbRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previous: String?) {
                CoroutineScope(Dispatchers.IO).launch {
                    val shoppingListModel: ShoppingListModel =
                        snapshot.getValue(ShoppingListModel::class.java) ?: return@launch
                    addList(ShoppingListConverter.listFromModel(shoppingListModel))
                    withContext(Dispatchers.Main) {
                        dataChanged()
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previous: String?) {
                CoroutineScope(Dispatchers.IO).launch {
                    val shoppingListModel: ShoppingListModel =
                        snapshot.getValue(ShoppingListModel::class.java) ?: return@launch
                    val shoppingList = ShoppingListConverter.listFromModel(shoppingListModel)
                    shoppingLists.remove(shoppingList)
                    shoppingLists.add(shoppingList)
                    withContext(Dispatchers.Main) {
                        dataChanged()
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                CoroutineScope(Dispatchers.IO).launch {
                    val shoppingListModel: ShoppingListModel =
                        snapshot.getValue(ShoppingListModel::class.java) ?: return@launch
                    val shoppingList = ShoppingListConverter.listFromModel(shoppingListModel)
                    if (!idsRegistry.contains(shoppingList.id)) {
                        return@launch
                    }
                    shoppingLists.remove(shoppingList)
                    idsRegistry.remove(shoppingList.id)
                    withContext(Dispatchers.Main) {
                        dataChanged()
                    }
                }
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
        val current = shoppingListsToView[position]
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

    override fun getItemCount(): Int = shoppingListsToView.size

    private fun dataChanged() {
        shoppingListsToView = shoppingLists.toList()
        notifyDataSetChanged()
    }

    private fun addList(shoppingList: ShoppingList) {
        if (idsRegistry.contains(shoppingList.id)) {
            return
        }
        shoppingLists.add(shoppingList)
        idsRegistry.add(shoppingList.id)
    }

}