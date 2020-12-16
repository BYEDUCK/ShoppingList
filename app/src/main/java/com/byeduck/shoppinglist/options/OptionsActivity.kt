package com.byeduck.shoppinglist.options

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.byeduck.shoppinglist.MainActivity
import com.byeduck.shoppinglist.R
import com.byeduck.shoppinglist.common.PREF_FILE_NAME
import com.byeduck.shoppinglist.databinding.ActivityOptionsBinding

class OptionsActivity : AppCompatActivity() {

    private val intPreferencesCache = HashMap<String, Int>()
    private lateinit var sharedPreferences: SharedPreferences

    private val availableColorOptions = arrayOf(
        Color("white", R.color.white),
        Color("red", R.color.red),
        Color("green", R.color.green),
        Color("black", R.color.black, R.color.white)
    )

    private val indexByColor = HashMap<Int, Int>()

    init {
        for ((idx, color) in availableColorOptions.withIndex()) {
            indexByColor[color.background] = idx
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE)
        binding.listsColorPicker.adapter = getColorSpinnerAdapter()
        binding.listElementsColorPicker.adapter = getColorSpinnerAdapter()
        binding.listsColorPicker
            .setSelection(indexByColor[sharedPreferences.getInt("listColor", R.color.white)] ?: 0)
        binding.listElementsColorPicker
            .setSelection(indexByColor[sharedPreferences.getInt("elemColor", R.color.white)] ?: 0)
        binding.listsColorPicker.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    intPreferencesCache["listColor"] = availableColorOptions[position].background
                    intPreferencesCache["listTxtColor"] = availableColorOptions[position].text
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // do nothing
                }

            }
        binding.listElementsColorPicker.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    intPreferencesCache["elemColor"] = availableColorOptions[position].background
                    intPreferencesCache["elemTxtColor"] = availableColorOptions[position].text
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // do nothing
                }

            }
    }

    private fun getColorSpinnerAdapter() = ArrayAdapter(
        this, android.R.layout.simple_spinner_dropdown_item, availableColorOptions.map { it.name }
    )

    inner class Color(val name: String, val background: Int, val text: Int = R.color.black)

    fun saveSettings(ignored: View) {
        val preferencesEditor = sharedPreferences.edit()
        for (intPreference in intPreferencesCache) {
            preferencesEditor.putInt(intPreference.key, intPreference.value)
        }
        preferencesEditor.apply()
        val backToMainIntent = Intent(this, MainActivity::class.java)
        startActivity(backToMainIntent)
    }

    fun cancel(ignored: View) {
        val backToMainIntent = Intent(this, MainActivity::class.java)
        startActivity(backToMainIntent)
    }
}