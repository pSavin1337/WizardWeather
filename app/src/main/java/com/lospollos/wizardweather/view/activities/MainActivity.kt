package com.lospollos.wizardweather.view.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.lospollos.wizardweather.R
import com.lospollos.wizardweather.view.fragments.CityListFragment
import com.lospollos.wizardweather.view.fragments.WeatherCardsFragment

class MainActivity : AppCompatActivity() {

    lateinit var selectedCity: String
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //openCityListFragment()

        selectedCity = savedInstanceState?.getString("selectedCity").toString()
        setContentView(R.layout.activity_main)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
    }

    fun closeWeatherCardsFragment() =
        navController.popBackStack()

    fun openWeatherCardsFragment(cityName: String) {
        val args = Bundle()
        args.putString("cityName", cityName)
        navController.navigate(R.id.weatherCardsFragment, args)
    }

    /*private fun closeCityListFragment() =
        supportFragmentManager.findFragmentById(R.id.city_list_fragment)?.let {
            supportFragmentManager.beginTransaction()
                .remove(it).commit()
        }*/

    /*private fun openCityListFragment() =
        supportFragmentManager.beginTransaction()
            .replace(R.id.city_list_fragment, CityListFragment()).commit()*/

    /*override fun onBackPressed() {
        if (backPressed + 2000 > System.currentTimeMillis())
            super.onBackPressed()
        else
            Toast.makeText(baseContext, getString(R.string.double_back), Toast.LENGTH_SHORT).show()
        backPressed = System.currentTimeMillis()
    }*/

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("selectedCity", selectedCity)
    }

    /*override fun onDestroy() {
        super.onDestroy()
        closeCityListFragment()
    }*/

}