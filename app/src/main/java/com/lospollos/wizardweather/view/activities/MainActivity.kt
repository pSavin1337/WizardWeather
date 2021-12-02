package com.lospollos.wizardweather.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lospollos.wizardweather.R
import com.lospollos.wizardweather.view.ItemTouchHelperCallback
import com.lospollos.wizardweather.view.MainRecyclerAdapter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = MainRecyclerAdapter(getListCities()) {
            intent = Intent(this, WeatherCardsActivity::class.java)
            intent.putExtra("cityName", it)
            startActivity(intent)
        }

        val recyclerView : RecyclerView = findViewById(R.id.mainRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)


        val callback: ItemTouchHelper.Callback = ItemTouchHelperCallback(adapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(recyclerView)

        recyclerView.adapter = adapter
        //TODO("Пофиксить сохранение изменений d&d при обновлении экрана")
    }

    private fun getListCities() = this.resources.getStringArray(R.array.cities).toList()

}