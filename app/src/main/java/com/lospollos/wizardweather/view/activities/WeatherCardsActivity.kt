package com.lospollos.wizardweather.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.lospollos.wizardweather.R
import com.lospollos.wizardweather.view.ViewPagerAdapter

class WeatherCardsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_cards)

        val selectedCityName = intent.extras?.getString("cityName")

        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        val adapter = ViewPagerAdapter(selectedCityName)
        viewPager.adapter = adapter
    }
}