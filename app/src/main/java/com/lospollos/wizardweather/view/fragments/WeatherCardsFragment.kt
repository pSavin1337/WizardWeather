package com.lospollos.wizardweather.view.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.lospollos.wizardweather.R
import com.lospollos.wizardweather.model.network.BaseItemAdapterItem
import com.lospollos.wizardweather.view.ViewPagerAdapter
import com.lospollos.wizardweather.viewmodel.ViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WeatherCardsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather_cards, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectedCityName = arguments?.getString("cityName").toString()

        viewModel = ViewModelProvider(
            this,
            defaultViewModelProviderFactory)[ViewModel::class.java]
        viewModel.loadWeather(selectedCityName)
        observeViewModel()

        progressBar = view.findViewById(R.id.progressBar)
        viewPager = view.findViewById(R.id.viewPager)
    }

    private fun observeViewModel() = with(viewModel) {
        weatherItems.observe(viewLifecycleOwner) {
            apiResponse = it
        }
        isLoading.observe(viewLifecycleOwner) {
            if(it) {
                viewPager.visibility = View.INVISIBLE
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.INVISIBLE
                viewPager.visibility = View.VISIBLE
            }
        }
        icon.observe(viewLifecycleOwner) {
            adapter = ViewPagerAdapter(selectedCityName, {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, it)
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }, {
                view?.elevation = 0f
            })
            viewPager.adapter = adapter
            adapter.icon = it
            adapter.apiResponse = apiResponse
        }
        message.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WeatherCardsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WeatherCardsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}