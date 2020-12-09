package com.byeduck.shoppinglist.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.byeduck.shoppinglist.MainActivity
import com.byeduck.shoppinglist.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            val goToMainIntent = Intent(applicationContext, MainActivity::class.java)
            startActivity(goToMainIntent)
        }
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun login(ignored: View) {
        binding.loginBtn.isEnabled = false
        binding.signUpBtn.isEnabled = false
        val email = binding.emailEditTxt.text.toString()
        val password = binding.passwordEditTxt.text.toString()
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val goToMainIntent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(goToMainIntent)
                } else {
                    Log.e("LOG IN", "Log in failed", it.exception)
                    Toast.makeText(applicationContext, "Log in failed", Toast.LENGTH_LONG).show()
                    binding.loginBtn.isEnabled = true
                    binding.signUpBtn.isEnabled = true
                }
            }
    }

    fun signUp(ignored: View) {
        binding.loginBtn.isEnabled = false
        binding.signUpBtn.isEnabled = false
        val email = binding.emailEditTxt.text.toString()
        val password = binding.passwordEditTxt.text.toString()
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(applicationContext, "Sign up completed", Toast.LENGTH_LONG)
                        .show()
                    binding.passwordEditTxt.setText("")
                } else {
                    Log.e("SIGN UP", "Sign up failed", it.exception)
                    Toast.makeText(applicationContext, "Sign up failed!", Toast.LENGTH_LONG).show()
                }
                binding.loginBtn.isEnabled = true
                binding.signUpBtn.isEnabled = true
            }
    }
}