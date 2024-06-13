package com.max.openweatherapp.UI.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.max.openweatherapp.data.room.cityDataSource.City
import com.max.openweatherapp.databinding.ItemWeatherBoradcastBinding


class FavoritesBroadcastsAdapter(private val onClick: (city: FavoriteCity) -> Unit ) :
    ListAdapter<FavoriteCity, FavoritesBroadcastsAdapter.ViewHolder>(DIFFUTIL) {

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

        fun bind(city: FavoriteCity, onClick: (FavoriteCity)-> Unit) {              
            

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
        private val DIFFUTIL = object : DiffUtil.ItemCallback<FavoriteCity>() {

            override fun areItemsTheSame(oldItem: FavoriteCity, newItem: FavoriteCity): Boolean {
                return oldItem.cityId == newItem.cityId
            }

            override fun areContentsTheSame(oldItem: FavoriteCity, newItem: FavoriteCity): Boolean {
                return oldItem == newItem
            }
        }
    }
}