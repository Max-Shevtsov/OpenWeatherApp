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
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val viewModel: MainViewModel by viewModels()


        binding.button.setOnClickListener {
            var city = binding.editText.text.toString()

            viewModel._gCity.value = city
            viewModel.isButtonClicked.value = true
            Log.e("!!!", "button was clicked with City of ${viewModel._gCity.value}")
            viewModel.getResponseByClick()
        }
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.uiState.collect { state ->
                Log.e("!!!", "SetContentView state: ${state.main}")
                binding.textView.text = state.main.toString()
            }
        }


    }
}