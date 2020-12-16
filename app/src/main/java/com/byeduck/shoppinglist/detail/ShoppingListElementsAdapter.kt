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
import com.byeduck.shoppinglist.databinding.ListelemShoppingElemBinding
import com.byeduck.shoppinglist.model.ShoppingElementModel
import com.byeduck.shoppinglist.model.view.ShoppingElement
import com.byeduck.shoppinglist.util.ShoppingListConverter
import com.byeduck.shoppinglist.util.ShoppingListsViewModel
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentSkipListSet

class ShoppingListElementsAdapter(
    private val context: Context,
    private val viewModel: ShoppingListsViewModel,
    private val fragmentManager: FragmentManager,
    private val elemColor: Int,
    private val elemTxtColor: Int,
    private val listId: String
) : RecyclerView.Adapter<ShoppingListElementsAdapter.ShoppingListElementsViewHolder>() {

    private val idsRegistry = ConcurrentSkipListSet<String>()
    private var shoppingElementsToView: List<ShoppingElement> = emptyList()
    private val shoppingElements =
        ConcurrentSkipListSet<ShoppingElement> { s1, s2 -> s1.id.compareTo(s2.id) }

    inner class ShoppingListElementsViewHolder(val binding: ListelemShoppingElemBinding) :
        RecyclerView.ViewHolder(binding.root)

    init {
        val dbRef = viewModel.getDbListElemRef(listId)
        dbRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                CoroutineScope(Dispatchers.IO).launch {
                    val shoppingElemModel: ShoppingElementModel =
                        snapshot.getValue(ShoppingElementModel::class.java) ?: return@launch
                    addElem(ShoppingListConverter.elemFromModel(shoppingElemModel))
                    withContext(Dispatchers.Main) {
                        dataChanged()
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                CoroutineScope(Dispatchers.IO).launch {
                    val shoppingElemModel: ShoppingElementModel =
                        snapshot.getValue(ShoppingElementModel::class.java) ?: return@launch
                    val shoppingElem = ShoppingListConverter.elemFromModel(shoppingElemModel)
                    shoppingElements.remove(shoppingElem)
                    shoppingElements.add(shoppingElem)
                    withContext(Dispatchers.Main) {
                        dataChanged()
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                CoroutineScope(Dispatchers.IO).launch {
                    val shoppingElemModel: ShoppingElementModel =
                        snapshot.getValue(ShoppingElementModel::class.java) ?: return@launch
                    if (!idsRegistry.contains(shoppingElemModel.id)) {
                        return@launch
                    }
                    val shoppingElem = ShoppingListConverter.elemFromModel(shoppingElemModel)
                    shoppingElements.remove(shoppingElem)
                    idsRegistry.remove(shoppingElem.id)
                    withContext(Dispatchers.Main) {
                        dataChanged()
                    }
                }
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
    ): ShoppingListElementsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListelemShoppingElemBinding.inflate(inflater, parent, false)
        binding.root.setBackgroundColor(elemColor)
        binding.listElemPriceLbl.setTextColor(elemTxtColor)
        binding.listElemCountLbl.setTextColor(elemTxtColor)
        binding.listElemTextLbl.setTextColor(elemTxtColor)
        return ShoppingListElementsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShoppingListElementsViewHolder, position: Int) {
        val current = shoppingElementsToView[position]
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

    override fun getItemCount(): Int = shoppingElementsToView.size

    private fun addElem(elem: ShoppingElement) {
        if (idsRegistry.contains(elem.id)) {
            return
        }
        shoppingElements.add(elem)
        idsRegistry.add(elem.id)
    }

    private fun dataChanged() {
        shoppingElementsToView = shoppingElements.toList()
        notifyDataSetChanged()
    }
}