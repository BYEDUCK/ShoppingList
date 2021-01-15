package com.byeduck.shoppinglist.shops.promo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.byeduck.shoppinglist.common.converter.JsonConverter
import com.byeduck.shoppinglist.common.converter.ShopConverter
import com.byeduck.shoppinglist.common.viewmodel.PromoViewModel
import com.byeduck.shoppinglist.databinding.ActivityAddEditViewPromotionBinding
import com.byeduck.shoppinglist.model.PromotionModel
import com.byeduck.shoppinglist.model.view.Promotion
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class AddEditViewPromotionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditViewPromotionBinding
    private lateinit var viewModel: PromoViewModel
    private lateinit var shopId: String
    private lateinit var shopName: String
    private var viewOnly = false
    private var promoId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditViewPromotionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(PromoViewModel::class.java)
        shopId = intent.getStringExtra("shopId") ?: ""
        shopName = intent.getStringExtra("shopName") ?: ""
        val promoJson = intent?.getStringExtra("promo") ?: ""
        viewOnly = intent?.getBooleanExtra("view", false) ?: false
        val promoId = intent?.getStringExtra("promoId") ?: ""
        if (promoJson.isNotEmpty()) {
            val promo = JsonConverter.gson().fromJson(promoJson, Promotion::class.java)
            this.promoId = promo.id
            shopId = promo.shopId
            shopName = promo.shopName
            populateFields(promo)
        } else if (promoId.isNotEmpty() && shopId.isNotEmpty()) {
            viewModel.getDbPromosRef(shopId)
                .child(promoId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val promoModel = snapshot.getValue(PromotionModel::class.java) ?: return
                        val promo = ShopConverter.promotionFromModel(promoModel)
                        populateFields(promo)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        goBackToPromos()
                    }

                })
        }
    }

    fun addEditPromo(ignored: View) {
        val promoDateStr = binding.promoDateTxt.text.toString()
        try {
            val promoDate = LocalDate.parse(promoDateStr, DateTimeFormatter.ISO_DATE)
            val promoName = binding.promoNameTxt.text.toString()
            val promoShortDesc = binding.promoShortDescTxt.text.toString()
            val promoFullDesc = binding.promoFullDescTxt.text.toString()
            if (promoId == null) {
                viewModel.addPromo(
                    promoName, promoShortDesc, promoFullDesc, shopId, shopName, promoDateStr
                )
                    .addOnSuccessListener {
                        goBackToPromos()
                    }

            } else {
                viewModel.updatePromo(
                    Promotion(
                        promoId!!,
                        shopId,
                        shopName,
                        promoName,
                        promoShortDesc,
                        promoFullDesc,
                        promoDate!!
                    )
                ).addOnSuccessListener {
                    goBackToPromos()
                }
            }
        } catch (e: DateTimeParseException) {
            Toast.makeText(
                this,
                "Please provide valid date in format yyyy-mm-dd",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    fun cancel(ignored: View) {
        goBackToPromos()
    }

    private fun goBackToPromos() {
        val goBackToPromosIntent = Intent(this, PromoActivity::class.java).apply {
            putExtra("shopId", shopId)
            putExtra("shopName", shopName)
        }
        startActivity(goBackToPromosIntent)
    }

    private fun populateFields(promo: Promotion) {
        binding.apply {
            promoDateTxt.setText(promo.date.toString())
            promoFullDescTxt.setText(promo.fullDescription)
            promoNameTxt.setText(promo.name)
            promoShortDescTxt.setText(promo.shortDescription)
        }
        if (viewOnly) {
            binding.apply {
                promoDateTxt.isEnabled = false
                promoFullDescTxt.isEnabled = false
                promoNameTxt.isEnabled = false
                promoShortDescTxt.isEnabled = false
                actionButtons.visibility = View.INVISIBLE
            }
        }
    }
}