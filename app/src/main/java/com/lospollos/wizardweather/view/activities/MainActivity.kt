package com.lospollos.wizardweather.view.activities

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import com.lospollos.wizardweather.App
import com.lospollos.wizardweather.R
import com.lospollos.wizardweather.view.City
import com.lospollos.wizardweather.view.fragments.WeatherCardsFragment
import com.lospollos.wizardweather.viewmodel.ViewModel
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    lateinit var selectedCity: String
    private lateinit var viewModel: ViewModel
    private var backPressed: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            defaultViewModelProviderFactory)[ViewModel::class.java]
        if(lifecycle.currentState == Lifecycle.State.INITIALIZED) {
            viewModel.getCityList()
            viewModel.getCityListLiveData().observe(this) { cityList ->
                if (cityList?.isEmpty() == true) {
                    resources?.getStringArray(R.array.cities)?.forEach {
                        App.cities.add(City(it, false))
                    }
                } else {
                    App.cities = cityList as ArrayList<City>
                }
            }
        }
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

    override fun onPause() {
        super.onPause()
        viewModel.updateCityList(App.cities)
    }

}