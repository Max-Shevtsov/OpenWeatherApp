package com.max.openweatherapp.ui.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.max.openweatherapp.data.room.favoritesDataSource.FavoriteCity
import com.max.openweatherapp.databinding.ItemWeatherBoradcastBinding
import kelvinToCelsiusConverter


class FavoritesBroadcastsAdapter(private val onClick: (favoriteCity: FavoriteCity) -> Unit ) :
    ListAdapter<FavoriteCity, FavoritesBroadcastsAdapter.ViewHolder>(DIFFUTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemWeatherBoradcastBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favoriteCity = getItem(position)
        holder.bind(favoriteCity, onClick)

    }

    class ViewHolder(
        private val binding: ItemWeatherBoradcastBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(favoriteCity: FavoriteCity, onClick: (FavoriteCity)-> Unit) {              
            

            with(binding) {
                itemCityName.text = favoriteCity.name
                itemCityTemp.text = kelvinToCelsiusConverter(favoriteCity.weatherParams.temp)
                itemCityWindSpeed.text = favoriteCity.wind.speed.toString()
                root.setOnClickListener { onClick(favoriteCity)

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