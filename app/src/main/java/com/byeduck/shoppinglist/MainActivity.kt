package com.byeduck.shoppinglist

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.byeduck.shoppinglist.databinding.ActivityMainBinding
import com.byeduck.shoppinglist.lists.ShoppingListsActivity
import com.byeduck.shoppinglist.login.LoggedInUser
import com.byeduck.shoppinglist.login.LoginActivity
import com.byeduck.shoppinglist.options.OptionsActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var user: LoggedInUser
    private var wasBackPressed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            user = LoggedInUser.getUser()
            val binding = ActivityMainBinding.inflate(layoutInflater)
            binding.userNameTxt.text = getString(R.string.welcome, user.userName)
            setContentView(binding.root)
        } catch (e: Exception) {
            val goToLoginIntent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(goToLoginIntent)
        }
    }

    fun goToShoppingLists(ignored: View) {
        val goToShoppingListsIntent = Intent(this, ShoppingListsActivity::class.java)
        startActivity(goToShoppingListsIntent)
    }

    fun goToSettings(ignored: View) {
        val goToSettingsIntent = Intent(this, OptionsActivity::class.java)
        startActivity(goToSettingsIntent)
    }

    override fun onBackPressed() {
        if (wasBackPressed) {
            FirebaseAuth.getInstance().signOut()
            val backToLoginIntent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(backToLoginIntent)
        } else {
            wasBackPressed = true
            Toast.makeText(applicationContext, "Press back again to log out", Toast.LENGTH_SHORT)
                .show()
            GlobalScope.launch {
                delay(1000)
                wasBackPressed = false
            }
        }
    }
}