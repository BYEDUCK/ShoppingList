package com.byeduck.shoppinglist.shops.promo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.byeduck.shoppinglist.common.converter.JsonConverter
import com.byeduck.shoppinglist.common.converter.ShopConverter
import com.byeduck.shoppinglist.common.viewmodel.PromoViewModel
import com.byeduck.shoppinglist.databinding.ActivityAddEditViewPromotionBinding
import com.byeduck.shoppinglist.model.PromotionModel
import com.byeduck.shoppinglist.model.ShopModel
import com.byeduck.shoppinglist.model.view.Promotion
import com.byeduck.shoppinglist.repository.ShoppingRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class AddEditViewPromotionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditViewPromotionBinding
    private lateinit var viewModel: PromoViewModel
    private val shopEssentials: MutableList<ShopEssential> = ArrayList()
    private val shopIdxByName = HashMap<String, Int>()
    private var viewOnly = false
    private var shopsInitialized = false
    private var promoId: String? = null


    inner class ShopEssential(
        val shopId: String,
        val shopName: String
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditViewPromotionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(PromoViewModel::class.java)
        val promoJson = intent?.getStringExtra("promo") ?: ""
        viewOnly = intent?.getBooleanExtra("view", false) ?: false
        val promoId = intent?.getStringExtra("promoId") ?: ""
        if (promoJson.isNotEmpty()) {
            val promo = JsonConverter.gson().fromJson(promoJson, Promotion::class.java)
            this.promoId = promo.id
            populateFields(promo)
        } else if (promoId.isNotEmpty()) {
            viewModel.getDbPromosRef()
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
        ShoppingRepository.getDbShopsRef()
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val shops = snapshot.getValue(object :
                        GenericTypeIndicator<Map<String, ShopModel>>() {})?.values ?: return
                    for ((idx, shop) in shops.withIndex()) {
                        shopEssentials.add(ShopEssential(shop.id, shop.name))
                        shopIdxByName[shop.name] = idx
                    }
                    binding.shopNamesSpinner.adapter = getShopNamesAdapter()
                    shopsInitialized = true
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@AddEditViewPromotionActivity,
                        "DB ERROR",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
    }

    fun addEditPromo(ignored: View) {
        if (!shopsInitialized) {
            return
        } else if (shopEssentials.isEmpty()) {
            Toast.makeText(this, "No shops available", Toast.LENGTH_SHORT).show()
            goBackToPromos()
        }
        val promoDateStr = binding.promoDateTxt.text.toString()
        try {
            val promoDate = LocalDate.parse(promoDateStr, DateTimeFormatter.ISO_DATE)
            val promoName = binding.promoNameTxt.text.toString()
            val promoShortDesc = binding.promoShortDescTxt.text.toString()
            val promoFullDesc = binding.promoFullDescTxt.text.toString()
            val selectedShopPos = binding.shopNamesSpinner.selectedItemPosition
            val shopId = shopEssentials[selectedShopPos].shopId
            val shopName = shopEssentials[selectedShopPos].shopName
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
        val goBackToPromosIntent = Intent(this, PromoActivity::class.java)
        startActivity(goBackToPromosIntent)
    }

    private fun getShopNamesAdapter() = ArrayAdapter(
        this, android.R.layout.simple_spinner_dropdown_item, shopEssentials.map { it.shopName }
    )

    private fun populateFields(promo: Promotion) {
        binding.apply {
            shopNamesSpinner.setSelection(shopIdxByName[promo.shopName] ?: 0)
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
                shopNamesSpinner.isEnabled = false
                actionButtons.visibility = View.INVISIBLE
            }
        }
    }
}