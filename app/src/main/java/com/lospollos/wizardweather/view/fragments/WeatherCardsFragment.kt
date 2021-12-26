package com.lospollos.wizardweather.view.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.lospollos.wizardweather.R
import com.lospollos.wizardweather.model.network.BaseItemAdapterItem
import com.lospollos.wizardweather.view.ViewPagerAdapter
import com.lospollos.wizardweather.view.activities.MainActivity
import com.lospollos.wizardweather.viewmodel.ViewModel

class WeatherCardsFragment : Fragment() {

    private lateinit var viewModel: ViewModel
    private lateinit var adapter: ViewPagerAdapter
    private lateinit var selectedCityName: String
    private lateinit var viewPager: ViewPager2
    private lateinit var progressBar: ProgressBar
    private lateinit var apiResponse: List<List<BaseItemAdapterItem>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather_cards, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            defaultViewModelProviderFactory
        )[ViewModel::class.java]
        selectedCityName = (activity as MainActivity).selectedCity
        progressBar = view.findViewById(R.id.progressBar)
        viewPager = view.findViewById(R.id.viewPager)
        getData()
        viewModel.loadWeather(selectedCityName)
    }

    private fun getData() {
        with(viewModel) {
            getWeatherItems().observe(viewLifecycleOwner) {
                apiResponse = it
            }
            getIsLoading().observe(viewLifecycleOwner) {
                if(it) {
                    viewPager.visibility = View.INVISIBLE
                    progressBar.visibility = View.VISIBLE
                } else {
                    progressBar.visibility = View.INVISIBLE
                    viewPager.visibility = View.VISIBLE
                }
            }
            getIcon().observe(viewLifecycleOwner) {
                adapter = ViewPagerAdapter(selectedCityName, {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, it)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    startActivity(shareIntent)
                }, { (activity as MainActivity).closeWeatherCardsFragment() })
                viewPager.adapter = adapter
                adapter.icon = it
                adapter.apiResponse = apiResponse
            }
            getMessage().observe(viewLifecycleOwner) {
                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
                (activity as MainActivity).closeWeatherCardsFragment()
            }
        }
    }

}