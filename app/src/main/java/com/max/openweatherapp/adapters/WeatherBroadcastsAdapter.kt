package com.max.openweatherapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.max.openweatherapp.MainViewModel
import com.max.openweatherapp.databinding.ItemWeatherBoradcastBinding
import com.max.openweatherapp.room.City
import com.max.openweatherapp.room.CityDatabase
import com.max.openweatherapp.room.CityRepository

class WeatherBroadcastsAdapter(private val cityActionListener: (cityId:Int) -> Unit ) :
    ListAdapter<City, WeatherBroadcastsAdapter.ViewHolder>(ALL_CITY) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemWeatherBoradcastBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val city = getItem(position)
        holder.bind(city, cityActionListener)

    }


    class ViewHolder(
        private val binding: ItemWeatherBoradcastBinding
    ) : RecyclerView.ViewHolder(binding.root) {


        fun bind(city: City, cityActionListener: (cityId: Int) -> Unit) {
            with(binding) {
                itemCityName.text = city.cityName
                itemCityTemp.text = city.cityTemp
                itemCityWindSpeed.text = city.cityWindSpeed
                itemDeleteButton.setOnClickListener { cityActionListener(city.cityId)

                }
            }
        }
    }

    companion object {
        private val ALL_CITY = object : DiffUtil.ItemCallback<City>() {
            override fun areItemsTheSame(oldItem: City, newItem: City): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: City, newItem: City): Boolean {
                return oldItem.cityName == newItem.cityName
            }
        }
    }

    interface cityActionListener {
        fun onCityDelete(city: City)
    }

}