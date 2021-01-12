package com.byeduck.shoppinglist.shops

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.byeduck.shoppinglist.R
import com.byeduck.shoppinglist.action.Action
import com.byeduck.shoppinglist.action.ShoppingActionsDialogFragment
import com.byeduck.shoppinglist.common.FirebaseRecyclerViewAdapterBase
import com.byeduck.shoppinglist.common.converter.ShopConverter
import com.byeduck.shoppinglist.common.viewmodel.ShopsViewModel
import com.byeduck.shoppinglist.databinding.ListelemShopBinding
import com.byeduck.shoppinglist.model.ShopModel
import com.byeduck.shoppinglist.model.view.Shop
import com.google.gson.Gson

class ShopsAdapter(
    private val viewModel: ShopsViewModel,
    private val context: Context,
    private val fragmentManager: FragmentManager
) : FirebaseRecyclerViewAdapterBase<ShopModel, Shop, ShopsAdapter.ShopsViewHolder>(
    viewModel.getDbShopsListRef(), { s1, s2 -> s1.id.compareTo(s2.id) }, ShopModel::class.java
) {

    inner class ShopsViewHolder(val binding: ListelemShopBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun viewItemFromModel(model: ShopModel): Shop {
        return ShopConverter.shopFromModel(model)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListelemShopBinding.inflate(inflater, parent, false)
        return ShopsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShopsViewHolder, position: Int) {
        val current = itemsToView[position]
        holder.binding.shopNameTxt.text = current.name
        holder.binding.shopDescriptionTxt.text = current.description
        holder.binding.shopLocationTxt.text = context.getString(
            R.string.latitude_longitude, current.latitude, current.longitude
        )
        holder.binding.shopRadiusTxt.text = current.radius.toString()
        holder.binding.root.setOnClickListener {
            val intent = Intent(context, AddEditViewShopActivity::class.java)
            intent.apply {
                putExtra("shop", Gson().toJson(current))
                putExtra("view", true)
            }
            context.startActivity(intent)
        }
        holder.binding.root.setOnLongClickListener {
            val dialog = ShoppingActionsDialogFragment { action ->
                when (action) {
                    Action.DELETE -> viewModel.deleteShopById(current.id)
                    Action.EDIT -> {
                        val intent = Intent(context, AddEditViewShopActivity::class.java)
                        intent.putExtra("shop", Gson().toJson(current))
                        context.startActivity(intent)
                    }
                }
            }
            dialog.show(fragmentManager, "dialog")
            true
        }
    }

}