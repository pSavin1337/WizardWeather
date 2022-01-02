package com.lospollos.wizardweather.view.activities

import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lospollos.wizardweather.R
import com.lospollos.wizardweather.view.fragments.CityListFragment
import com.lospollos.wizardweather.view.fragments.WeatherCardsFragment

class MainActivity : AppCompatActivity() {

    lateinit var selectedCity: String
    private var backPressed: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        openCityListFragment()

        selectedCity = savedInstanceState?.getString("selectedCity").toString()
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            setContentView(R.layout.activity_main_landscape)
        else
            setContentView(R.layout.activity_main)
    }

    fun closeWeatherCardsFragment() =
        supportFragmentManager.findFragmentById(R.id.weather_cards_fragment)?.let {
            supportFragmentManager.beginTransaction()
                .remove(it).commit()
        }

    fun openWeatherCardsFragment(cityName: String) {
        selectedCity = cityName
        supportFragmentManager.beginTransaction()
            .replace(R.id.weather_cards_fragment, WeatherCardsFragment()).commit()
    }

    private fun closeCityListFragment() =
        supportFragmentManager.findFragmentById(R.id.city_list_fragment)?.let {
            supportFragmentManager.beginTransaction()
                .remove(it).commit()
        }

    private fun openCityListFragment() =
        supportFragmentManager.beginTransaction()
            .replace(R.id.city_list_fragment, CityListFragment()).commit()

    override fun onBackPressed() {
        if (backPressed + 2000 > System.currentTimeMillis())
            super.onBackPressed()
        else
            Toast.makeText(baseContext, getString(R.string.double_back), Toast.LENGTH_SHORT).show()
        backPressed = System.currentTimeMillis()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("selectedCity", selectedCity)
    }

    override fun onDestroy() {
        super.onDestroy()
        closeCityListFragment()
    }

}