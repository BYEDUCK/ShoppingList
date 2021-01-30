package com.byeduck.shoppinglist.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import android.widget.Toast
import com.byeduck.shoppinglist.R
import com.byeduck.shoppinglist.common.converter.ShopConverter
import com.byeduck.shoppinglist.model.ShopModel
import com.byeduck.shoppinglist.model.view.Shop
import com.byeduck.shoppinglist.repository.ShoppingRepository
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import java.util.*

class FavouriteShopsRemoteViewsService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        val widgetId = intent?.getIntExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: throw IllegalStateException()
        return FavouriteShopsRemoteViewsFactory(applicationContext, widgetId)
    }

    class FavouriteShopsRemoteViewsFactory(
        private val context: Context,
        private val widgetId: Int
    ) : RemoteViewsFactory {

        private val appWidgetManager = AppWidgetManager.getInstance(context)
        private val shops = TreeSet<Shop> { s1, s2 -> s1.id.compareTo(s2.id) }
        private var shopsToView: Array<Shop> = emptyArray()

        private val dbListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.child("favourite").value == true) {
                    val model = snapshot.getValue(ShopModel::class.java) ?: return
                    val shop = ShopConverter.shopFromModel(model)
                    if (!shops.contains(shop)) {
                        shops.add(shop)
                        shopsToView = shops.toArray(emptyArray())
                        appWidgetManager.notifyAppWidgetViewDataChanged(
                            widgetId,
                            R.id.favouriteShopsList
                        )
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.child("favourite").value == true) {
                    val model = snapshot.getValue(ShopModel::class.java) ?: return
                    val shop = ShopConverter.shopFromModel(model)
                    if (shops.contains(shop)) {
                        shops.remove(shop)
                        shops.add(shop)
                        shopsToView = shops.toArray(emptyArray())
                        appWidgetManager.notifyAppWidgetViewDataChanged(
                            widgetId,
                            R.id.favouriteShopsList
                        )
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                if (snapshot.child("favourite").value == true) {
                    val model = snapshot.getValue(ShopModel::class.java) ?: return
                    val shop = ShopConverter.shopFromModel(model)
                    if (shops.contains(shop)) {
                        shops.remove(shop)
                        shopsToView = shops.toArray(emptyArray())
                        appWidgetManager.notifyAppWidgetViewDataChanged(
                            widgetId,
                            R.id.favouriteShopsList
                        )
                    }
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("DATASET", "CHILD MOVED")
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "DB ERROR", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onCreate() {
            ShoppingRepository.getDbShopsRef().addChildEventListener(dbListener)
        }

        override fun onDataSetChanged() {
            Log.d("DATASET", "CHANGED -> ${shopsToView.size}")
        }

        override fun onDestroy() {
            ShoppingRepository.getDbShopsRef().removeEventListener(dbListener)
        }

        override fun getCount(): Int {
            return shopsToView.size
        }

        override fun getViewAt(position: Int): RemoteViews {
            val shop = shopsToView[position]
            val remoteViews = RemoteViews(context.packageName, R.layout.listelem_shop)
            remoteViews.setTextViewText(R.id.shopNameTxt, shop.name)
            remoteViews.setTextViewText(R.id.shopDescriptionTxt, shop.description)
            remoteViews.setTextViewText(
                R.id.shopLocationTxt,
                context.getString(R.string.latitude_longitude, shop.latitude, shop.longitude)
            )
            remoteViews.setTextViewText(R.id.shopRadiusTxt, shop.radius.toString())
            val fillInIntent = Intent().apply {
                putExtra("shopId", shop.id)
            }
            remoteViews.setOnClickFillInIntent(R.id.shopListElem, fillInIntent)
            return remoteViews
        }

        override fun getLoadingView(): RemoteViews =
            RemoteViews(context.packageName, R.layout.listelem_loading)

        override fun getViewTypeCount(): Int = 1

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun hasStableIds(): Boolean = false

    }
}