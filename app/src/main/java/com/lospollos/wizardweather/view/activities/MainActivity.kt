package com.lospollos.wizardweather.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lospollos.wizardweather.R
import com.lospollos.wizardweather.view.MainRecyclerAdapter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView : RecyclerView = findViewById(R.id.mainRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MainRecyclerAdapter(getListCities()) {
            intent = Intent(this, WeatherCardsActivity::class.java)
            intent.putExtra("cityName", it)
            startActivity(intent)
        }
    }

    private fun getListCities() = this.resources.getStringArray(R.array.cities).toList()

}