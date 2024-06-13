package com.max.openweatherapp

import FavoritesFragment
import WeatherFragment
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.max.openweatherapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private var viewBinding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = viewBinding!!


    private val viewModel: viewModel by viewModels {
        viewModel.Factory
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("!!!", "onCreate: $this")

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        val intent = intent
        supportFragmentManager.commit {
            replace<WeatherFragment>(R.id.fragment_container_view)
        }

        setContentView(view)
        setSupportActionBar(binding.toolBar)
        handleIntent(intent)
        initListeners()

    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {

        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                viewModel.getWeatherBroadcast(query)
                // добавить навигацию к weatherFragment?
                supportFragmentManager.commit {
                    replace<WeatherFragment>(R.id.fragment_container_view)
                }
            }
        }
    }

    private fun initListeners() {
        binding.toolBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_favorites -> {
                    //nav to favorites_fragment
                    supportFragmentManager.commit {
                        replace<FavoritesFragment>(R.id.fragment_container_view)
                    }
                    true
                }

                else -> false
            }
        }
    }

    fun replaceWeatherFragment() {

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        val component = componentName
        val searchableInfo = searchManager.getSearchableInfo(component)
        searchView.setSearchableInfo(searchableInfo)


        return super.onCreateOptionsMenu(menu)
    }
}
