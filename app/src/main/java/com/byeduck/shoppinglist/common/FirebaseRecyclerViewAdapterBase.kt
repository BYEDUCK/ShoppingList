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
import java.util.concurrent.ConcurrentSkipListSet

abstract class FirebaseRecyclerViewAdapterBase<ModelType, ViewType, ViewHolderType>(
    dbRef: DatabaseReference,
    comparator: Comparator<ViewType>,
    private val modelClass: Class<ModelType>
) : RecyclerView.Adapter<ViewHolderType>() where ModelType : Model, ViewHolderType : RecyclerView.ViewHolder {

    private val idsRegistry = ConcurrentSkipListSet<String>()
    private val items = ConcurrentSkipListSet(comparator)
    var itemsToView: List<ViewType> = emptyList()

    init {
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
                Log.d("FirebaseRecyclerViewAdapter", "Child moved: ${snapshot.value}")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseRecyclerViewAdapter", "DB error", error.toException())
            }

        })
    }

    fun childAdded(snapshot: DataSnapshot) {
        CoroutineScope(Dispatchers.IO).launch {
            val model = snapshot.getValue(modelClass) ?: return@launch
            addItem(model)
            withContext(Dispatchers.Main) {
                dataChanged()
            }
        }
    }

    fun childChanged(snapshot: DataSnapshot) {
        CoroutineScope(Dispatchers.IO).launch {
            val model = snapshot.getValue(modelClass) ?: return@launch
            val viewItem = viewItemFromModel(model)
            items.remove(viewItem)
            items.add(viewItem)
            withContext(Dispatchers.Main) {
                dataChanged()
            }
        }
    }

    fun childRemoved(snapshot: DataSnapshot) {
        CoroutineScope(Dispatchers.IO).launch {
            val model = snapshot.getValue(modelClass) ?: return@launch
            if (!idsRegistry.contains(model.id)) {
                return@launch
            }
            val viewItem = viewItemFromModel(model)
            items.remove(viewItem)
            idsRegistry.remove(model.id)
            withContext(Dispatchers.Main) {
                dataChanged()
            }
        }
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

    abstract fun viewItemFromModel(model: ModelType): ViewType

}