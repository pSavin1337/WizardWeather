package com.lospollos.wizardweather.view.activities

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.lospollos.wizardweather.R
import com.lospollos.wizardweather.model.network.BaseItemAdapterItem
import com.lospollos.wizardweather.view.ViewPagerAdapter
import com.lospollos.wizardweather.viewmodel.ViewModel

class WeatherCardsActivity : AppCompatActivity() {

    private lateinit var viewModel: ViewModel
    private lateinit var adapter: ViewPagerAdapter
    private lateinit var selectedCityName: String
    private lateinit var viewPager: ViewPager2
    private lateinit var progressBar: ProgressBar
    private lateinit var apiResponse: List<List<BaseItemAdapterItem>>

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_cards)

        selectedCityName = intent.extras?.getString("cityName").toString()

        viewModel = ViewModelProvider(
            this,
            defaultViewModelProviderFactory)[ViewModel::class.java]
        viewModel.loadWeather(selectedCityName)
        observeViewModel()

        progressBar = findViewById(R.id.progressBar)
        viewPager = findViewById(R.id.viewPager)

    }

    private fun observeViewModel() = with(viewModel) {
        weatherItems.observe(this@WeatherCardsActivity) {
            apiResponse = it
        }
        isLoading.observe(this@WeatherCardsActivity) {
            if(it) {
                viewPager.visibility = View.INVISIBLE
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.INVISIBLE
                viewPager.visibility = View.VISIBLE
            }
        }
        icon.observe(this@WeatherCardsActivity) {
            adapter = ViewPagerAdapter(selectedCityName)
            viewPager.adapter = adapter
            adapter.icon = it
            adapter.apiResponse = apiResponse
        }
        message.observe(this@WeatherCardsActivity) {
            Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
        }
    }

}