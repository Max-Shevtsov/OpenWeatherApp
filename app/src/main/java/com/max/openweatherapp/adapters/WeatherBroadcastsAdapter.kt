package com.max.openweatherapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.max.openweatherapp.data.room.cityDataSource.City
import com.max.openweatherapp.databinding.ItemWeatherBoradcastBinding


class WeatherBroadcastsAdapter(private val onClick: (city: City) -> Unit ) :
    ListAdapter<City, WeatherBroadcastsAdapter.ViewHolder>(ALL_CITY) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemWeatherBoradcastBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val city = getItem(position)
        holder.bind(city, onClick)

    }

    class ViewHolder(
        private val binding: ItemWeatherBoradcastBinding
    ) : RecyclerView.ViewHolder(binding.root) {


//        init {
//            binding.root.setOnClickListener {
//                currentCity?.let {
//                    onClick(it)
//                }
//            }
//        }

        fun bind(city: City, onClick: (City)-> Unit) {              //onClick: (city) -> Unit)
            

            with(binding) {
                itemCityName.text = city.cityName
                itemCityTemp.text = city.cityTemp
                itemCityWindSpeed.text = city.cityWindSpeed
                root.setOnClickListener { onClick(city)

                }
            }
        }
    }

    companion object {
        private val ALL_CITY = object : DiffUtil.ItemCallback<City>() {

            override fun areItemsTheSame(oldItem: City, newItem: City): Boolean {
                return oldItem.cityId == newItem.cityId
            }

            override fun areContentsTheSame(oldItem: City, newItem: City): Boolean {
                return oldItem == newItem
            }
        }
    }
}