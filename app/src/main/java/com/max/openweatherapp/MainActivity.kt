package com.max.openweatherapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.max.openweatherapp.databinding.ActivityMainBinding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.max.openweatherapp.room.CityDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private var viewBinding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = viewBinding!!

    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory((application as CityApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)
        initListeners()
        renderState()
    }

    @SuppressLint("StringFormatMatches")
    private fun renderState() {
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.uiState.collect { state ->
                Log.e("!!!", "SetContentView state: ${state.main} and ${state.wind}")
                binding.textView.text = getString(
                    R.string.broadcast, state.main, state.wind
                ) // Вынес в строковые ресурсы
                binding.city.text = state.cityInDatabase.city.toString()
            }
        }
    }

    private fun initListeners() {
        binding.button.setOnClickListener {
            val city = binding.editText.text.toString()
            Log.e("!!!", "button was clicked with City of $city")
            viewModel.insert(city)
            viewModel.getWeatherBroadcast(city)
        }
    }
}