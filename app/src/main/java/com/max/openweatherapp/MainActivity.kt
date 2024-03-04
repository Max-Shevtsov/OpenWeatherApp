package com.max.openweatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.max.openweatherapp.databinding.ActivityMainBinding
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher


class MainActivity : AppCompatActivity() {
    private var viewBinding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = viewBinding!!

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initListeners()
        renderState()
    }

    private fun renderState() {
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.uiState.collect { state ->
                Log.e("!!!", "SetContentView state: ${state.temp} and ${state.pressure}")
                binding.textView.text = "Temperature:${state.temp} Pressure:${state.pressure}" // Вынести в строковые ресурсы
            }
        }
    }

    private fun initListeners() {
        binding.button.setOnClickListener {
            val city = binding.editText.text.toString()
            Log.e("!!!", "button was clicked with City of ${viewModel._gCity.value}")
            viewModel.getWeatherBroadcast(city)
        }
    }
}