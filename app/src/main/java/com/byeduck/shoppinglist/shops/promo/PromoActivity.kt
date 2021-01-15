package com.byeduck.shoppinglist.shops.promo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.byeduck.shoppinglist.MainActivity
import com.byeduck.shoppinglist.common.viewmodel.PromoViewModel
import com.byeduck.shoppinglist.databinding.ActivityPromoBinding

class PromoActivity : AppCompatActivity() {

    private lateinit var viewModel: PromoViewModel
    private lateinit var binding: ActivityPromoBinding
    private lateinit var shopId: String
    private lateinit var shopName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPromoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(PromoViewModel::class.java)
        shopId = intent?.getStringExtra("shopId") ?: ""
        shopName = intent?.getStringExtra("shopName") ?: ""
        binding.promosRecyclerView.adapter =
            PromoAdapter(this, viewModel, supportFragmentManager, shopId)
        binding.promosRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.promosRecyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    fun addPromo(ignored: View) {
        val addPromoIntent = Intent(this, AddEditViewPromotionActivity::class.java).apply {
            putExtra("shopId", shopId)
            putExtra("shopName", shopName)
        }
        startActivity(addPromoIntent)
    }

    override fun onBackPressed() {
        val goToMainIntent = Intent(this, MainActivity::class.java)
        startActivity(goToMainIntent)
    }
}