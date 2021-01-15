package com.byeduck.shoppinglist.shops.promo

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.byeduck.shoppinglist.action.Action
import com.byeduck.shoppinglist.action.ShoppingActionsDialogFragment
import com.byeduck.shoppinglist.common.FirebaseRecyclerViewAdapterBase
import com.byeduck.shoppinglist.common.converter.JsonConverter
import com.byeduck.shoppinglist.common.converter.ShopConverter
import com.byeduck.shoppinglist.common.viewmodel.PromoViewModel
import com.byeduck.shoppinglist.databinding.ListelemPromoBinding
import com.byeduck.shoppinglist.model.PromotionModel
import com.byeduck.shoppinglist.model.view.Promotion

class PromoAdapter(
    private val context: Context,
    private val viewModel: PromoViewModel,
    private val fragmentManager: FragmentManager,
    private val shopId: String
) :
    FirebaseRecyclerViewAdapterBase<PromotionModel, Promotion, PromoAdapter.PromoViewHolder>(
        viewModel.getDbPromosRef(shopId),
        { p1, p2 -> p1.date.compareTo(p2.date) },
        PromotionModel::class.java
    ) {

    inner class PromoViewHolder(val binding: ListelemPromoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun viewItemFromModel(model: PromotionModel): Promotion {
        return ShopConverter.promotionFromModel(model)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListelemPromoBinding.inflate(inflater, parent, false)
        return PromoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PromoViewHolder, position: Int) {
        val current = itemsToView[position]
        holder.binding.apply {
            promoNameTxt.text = current.name
            promoShortDescTxt.text = current.shortDescription
            promoDateTxt.text = current.date.toString()
        }
        holder.binding.root.setOnClickListener {
            val intent = Intent(context, AddEditViewPromotionActivity::class.java)
            intent.apply {
                putExtra("promo", JsonConverter.gson().toJson(current))
                putExtra("view", true)
            }
            context.startActivity(intent)
        }
        holder.binding.root.setOnLongClickListener {
            val dialog = ShoppingActionsDialogFragment { action ->
                when (action) {
                    Action.DELETE -> viewModel.deletePromoById(current.id, shopId)
                    Action.EDIT -> {
                        val intent = Intent(context, AddEditViewPromotionActivity::class.java)
                        intent.putExtra("promo", JsonConverter.gson().toJson(current))
                        context.startActivity(intent)
                    }
                }
            }
            dialog.show(fragmentManager, "dialog")
            true
        }
    }

}