package com.max.openweatherapp.UI.favorites

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.max.openweatherapp.viewModel
import com.max.openweatherapp.R
import com.max.openweatherapp.UI.weather.WeatherFragment
import com.max.openweatherapp.adapters.WeatherBroadcastsAdapter
import com.max.openweatherapp.databinding.FavoritesFragmentBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoritesFragment : Fragment(R.layout.favorites_fragment) {

    private var _binding: FavoritesFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: WeatherBroadcastsAdapter

    private val viewModel: FavoritesViewModel by viewModels{
        viewModel.Factory
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FavoritesFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        adapter = FavoritesBroadcastsAdapter { city ->

            parentFragmentManager.commit {
                replace<WeatherFragment>(R.id.fragment_container_view)
            }
        }

        val dividerItemDecoration = DividerItemDecoration(context, RecyclerView.VERTICAL)
        dividerItemDecoration.setDrawable(resources.getDrawable(R.drawable.divider_drawable))

        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(dividerItemDecoration)
        
        initListeners()
        renderState()

        return view
    }

    override fun onViewCreated(view: View, sevedInstanceState: Bundle?) {

    }

    private fun renderState() {
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.favoritesUiState.collect { state ->
                adapter.submitList(state.allCity)
                if (!state.isLoading)
                    binding.swipeRefresh.isRefreshing = false
                //if(state.errorMessage? != null)run {
                //val toast = Toast.makeText(context, state.errorMessage,).show()
                //}
            }
        }
    }

    private fun initListeners() {
        binding.swipeRefresh.setOnRefreshListener {
            Log.e("!!!", "onRefresh called from SwipeRefreshLayout")
            viewModel.refreshWeather()

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}