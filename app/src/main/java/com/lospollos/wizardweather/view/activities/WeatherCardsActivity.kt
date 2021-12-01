package com.lospollos.wizardweather.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.lospollos.wizardweather.R

class WeatherCardsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_cards)

        val textView: TextView = findViewById(R.id.textView)
        textView.text = intent.extras?.getString("cityName")
    }
}