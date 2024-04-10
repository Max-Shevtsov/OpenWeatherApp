package com.max.openweatherapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.max.openweatherapp.databinding.ActivityMainBinding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.max.openweatherapp.adapters.WeatherBroadcastsAdapter
import com.max.openweatherapp.room.CityDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private var viewBinding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = viewBinding!!

    private val adapter = WeatherBroadcastsAdapter()

    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory((application as CityApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        private val intent = getIntent()

        setContentView(view)
        initListeners()
        renderState()

    }

    @SuppressLint("StringFormatMatches")
    private fun renderState() {
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.uiState.collect { state ->
                Log.e("!!!", "SetContentView state: ${state.main} and ${state.wind}")
                binding.weatherBroadcast.text = getString(
                    R.string.broadcast, state.main.temp, state.wind.speed
                )

                adapter.submitList(state.city)

            }

        }
    }

    private fun initListeners() {
        binding.button.setOnClickListener {
            //val city = binding.editText.text.toString()
            Log.e("!!!", "button was clicked with City of $city")
            if (Intent.ACTION_SEARCH == intent.action) {
                intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                    viewModel.insert(query)
                    viewModel.getWeatherBroadcast(query)
                }
            }
        }
    }

    private fun onCreateOptionMenu(menu:Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        val component = ComponentName(this)
        val searchableInfo = searchManager.gerSearchableInfo(component)
        searchView.setSearchableInfo(searchableInfo)
        return true
    }

}