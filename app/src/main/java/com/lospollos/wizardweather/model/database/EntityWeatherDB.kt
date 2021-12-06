package com.lospollos.wizardweather.model.database

import android.graphics.drawable.Drawable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bumptech.glide.RequestBuilder

@Entity
class EntityWeatherDB(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var city: String,
    var date: String,
    var temperature: String,
    var pressure: String,
    var humidity: String,
    var windSpeed: String,
    var windDegree: String,
    var clouds: String,
    var weatherId: Int,
    var weatherDescription: String,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var icon: ByteArray
)
