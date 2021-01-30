package com.byeduck.shoppinglist.common

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.byeduck.shoppinglist.model.Model
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.HashSet

abstract class FirebaseRecyclerViewAdapterBase<ModelType, ViewType, ViewHolderType>(
    private val dbRef: DatabaseReference,
    comparator: Comparator<ViewType>,
    private val modelClass: Class<ModelType>
) : RecyclerView.Adapter<ViewHolderType>() where ModelType : Model, ViewHolderType : RecyclerView.ViewHolder {

    private val idsRegistry = HashSet<String>()
    private val items = TreeSet(comparator)
    var itemsToView: List<ViewType> = emptyList()

    private val dbListener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            CoroutineScope(Dispatchers.IO).launch {
                childAdded(snapshot)
                withContext(Dispatchers.Main) {
                    dataChanged()
                }
            }
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            CoroutineScope(Dispatchers.IO).launch {
                childChanged(snapshot)
                withContext(Dispatchers.Main) {
                    dataChanged()
                }
            }
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            CoroutineScope(Dispatchers.IO).launch {
                childRemoved(snapshot)
                withContext(Dispatchers.Main) {
                    dataChanged()
                }
            }
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            Log.d("FirebaseRecyclerViewAdapter", "Child moved: ${snapshot.value}")
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("FirebaseRecyclerViewAdapter", "DB error", error.toException())
        }

    }

    init {
        dbRef.addChildEventListener(dbListener)
    }

    fun removeListener() = dbRef.removeEventListener(dbListener)

    suspend fun childAdded(snapshot: DataSnapshot) {
        val model = snapshot.getValue(modelClass) ?: return
        addItem(model)
    }

    suspend fun childChanged(snapshot: DataSnapshot) {
        val model = snapshot.getValue(modelClass) ?: return
        val viewItem = viewItemFromModel(model)
        items.remove(viewItem)
        items.add(viewItem)
    }

    suspend fun childRemoved(snapshot: DataSnapshot) {
        val model = snapshot.getValue(modelClass) ?: return
        if (!idsRegistry.contains(model.id)) {
            return
        }
        val viewItem = viewItemFromModel(model)
        items.remove(viewItem)
        idsRegistry.remove(model.id)
    }

    private fun addItem(item: ModelType) {
        if (idsRegistry.contains(item.id)) {
            return
        }
        items.add(viewItemFromModel(item))
        idsRegistry.add(item.id)
    }

    private fun dataChanged() {
        itemsToView = items.toList()
        notifyDataSetChanged()
    }

    override fun getItemCount() = itemsToView.size

    abstract fun viewItemFromModel(model: ModelType): ViewType

}