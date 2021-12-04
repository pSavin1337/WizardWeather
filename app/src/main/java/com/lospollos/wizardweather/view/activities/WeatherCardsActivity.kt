package com.lospollos.wizardweather.view.activities

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.lospollos.wizardweather.R
import com.lospollos.wizardweather.model.BaseItemAdapterItem
import com.lospollos.wizardweather.view.ViewPagerAdapter
import com.lospollos.wizardweather.viewmodel.ViewModel
import com.lospollos.wizardweather.model.retrofit.Result

class WeatherCardsActivity : AppCompatActivity() {

    private lateinit var viewModel: ViewModel
    private lateinit var adapter: ViewPagerAdapter
    private var selectedCityName: String? = null

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_cards)

        selectedCityName = intent.extras?.getString("cityName")

        viewModel = ViewModelProvider(
            this,
            defaultViewModelProviderFactory)[ViewModel::class.java]
        viewModel.loadWeather(selectedCityName, this)
        observeViewModel()

    }

    private fun observeViewModel() = with(viewModel) {
        weatherItems.observe(this@WeatherCardsActivity) {
            val viewPager: ViewPager2 = findViewById(R.id.viewPager)
            adapter = ViewPagerAdapter(selectedCityName, it)
            viewPager.adapter = adapter
        }
        message.observe(this@WeatherCardsActivity) {
            Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
        }
    }

}