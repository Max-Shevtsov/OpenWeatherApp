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
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import com.max.openweatherapp.databinding.ActivityMainBinding
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.max.openweatherapp.adapters.WeatherBroadcastsAdapter
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
        Log.e("!!!", "onCreate: $this")

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        adapter = WeatherBroadcastsAdapter { cityId ->
            viewModel.deleteCityFromDb(cityId)
        }

        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        setContentView(view)
        setSupportActionBar(binding.toolBar)
        initListeners()
        renderState()

    }

    private fun renderState() {
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.uiState.collect { state ->
                adapter.submitList(state.allCity)
                if (!state.isLoading)
                    binding.swipeRefresh.isRefreshing = false
                //if(state.errorMessage? != null)run {
                //val toast = Toast.makeText(context, state.errorMessage,).show()
            }
        }
    }

    private fun initListeners() {
        binding.swipeRefresh.setOnRefreshListener {
            Log.e("!!!", "onRefresh called from SwipeRefreshLayout")
            viewModel.updateWeatherBroadcast()
            
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        val component = componentName
        val searchableInfo = searchManager.getSearchableInfo(component)
        searchView.setSearchableInfo(searchableInfo)

        try {
            if (Intent.ACTION_SEARCH == intent.action) {
                intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                    if (query.isNullOrEmpty()) {
                        Log.e("!!!", "e: isNullOrEmpty")
                    } else {
                        viewModel.getWeatherBroadcast(query)
                    }
                }
            }
        } catch (e: Throwable) {
            Log.e("!!!", "e: $e")
        }
        return super.onCreateOptionsMenu(menu)
    }

}
