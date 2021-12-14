package com.lospollos.wizardweather.view.activities

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lospollos.wizardweather.App
import com.lospollos.wizardweather.R
import com.lospollos.wizardweather.view.fragments.WeatherCardsFragment

class MainActivity : AppCompatActivity() {

    lateinit var selectedCity: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectedCity = savedInstanceState?.getString("selectedCity").toString()
        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            setContentView(R.layout.activity_main_landscape)
        else
            setContentView(R.layout.activity_main)
    }

    fun closeWeatherCardsFragment() {
        supportFragmentManager.findFragmentById(R.id.weather_cards_fragment)?.let {
            supportFragmentManager.beginTransaction()
                .remove(it).commit()
        }
    }

    fun openWeatherCardsFragment(cityName: String) {
        selectedCity = cityName
        supportFragmentManager.beginTransaction()
            .replace(R.id.weather_cards_fragment, WeatherCardsFragment()).commit()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("selectedCity", selectedCity)
    }
}