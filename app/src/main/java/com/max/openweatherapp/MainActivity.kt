package com.max.openweatherapp

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.LifecycleCoroutineScope
import com.max.openweatherapp.databinding.ActivityMainBinding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.max.openweatherapp.adapters.WeatherBroadcastsAdapter
import com.max.openweatherapp.room.CityDatabase
import com.max.openweatherapp.room.CityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private var viewBinding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = viewBinding!!


    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory((application as CityApplication).repository)
    }

    private lateinit var adapter: WeatherBroadcastsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        adapter = WeatherBroadcastsAdapter { cityId ->
            lifecycleScope.launch {
                viewModel.deleteCityFromDb(cityId)
            }

        }

        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        setContentView(view)
        setSupportActionBar(binding.toolBar)
        initListeners()
        renderState()

    }

    @SuppressLint("StringFormatMatches")
    private fun renderState() {
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.uiState.collect { state ->
                Log.e("!!!", "SetContentView state: ${state.main} and ${state.wind}")
//                binding.weatherBroadcast.text = getString(
//                    R.string.broadcast, state.main.temp, state.wind.speed
//                )
                adapter.submitList(state.city)

            }

        }
    }

    private fun initListeners() {
       // binding.button.setOnClickListener {
            //val city = binding.editText.text.toString()
       // }
        //Log.e("!!!", "button was clicked with City of $city")
        val intent = intent



        binding.swipeRefresh.setOnRefreshListener {
            Log.e("!!!", "onRefresh called from SwipeRefreshLayout") 
            viewModel.updateWeatherBroadcast()
        }
    }

    override fun onCreateOptionsMenu(menu:Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        val component = componentName
        val searchableInfo = searchManager.getSearchableInfo(component)
        searchView.setSearchableInfo(searchableInfo)

        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                viewModel.getWeatherBroadcast(query)
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

}
