package com.max.openweatherapp.ui.weather


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.max.openweatherapp.R
import com.max.openweatherapp.databinding.WeatherFragmentBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import loadWeatherTypePicture


class WeatherFragment : Fragment(R.layout.weather_fragment) {

    private var _binding: WeatherFragmentBinding? = null
    private val binding get() = _binding!!

    private val weatherViewModel: WeatherViewModel by viewModels {
        WeatherViewModel.createFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = WeatherFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, sevedInstanceState: Bundle?) {
        initListeners()
        renderState()
    }

    private fun renderState() {
        lifecycleScope.launch(Dispatchers.Main) {
            weatherViewModel.weatherUiState.collect { state ->
                binding.cityName.text = state.city?.cityName
                binding.cityTemp.text = state.city?.cityTemp
                binding.cityWindSpeed.text = state.city?.cityWindSpeed
                binding.starButton.isChecked = state.city?.isStarred ?: false
                loadWeatherTypePicture(state.city?.icon, binding.weatherType)
            }
        }
    }

    private fun initListeners() {

        binding.starButton.setOnClickListener {
            if (binding.starButton.isChecked) {
                weatherViewModel.putCityIntoFavorites()
            } else {
                weatherViewModel.deleteCityFromFavorites()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


