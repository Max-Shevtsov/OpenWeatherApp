import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.max.openweatherapp.MainViewModel
import com.max.openweatherapp.R
import com.max.openweatherapp.adapters.WeatherBroadcastsAdapter
import com.max.openweatherapp.databinding.FavoritesFragmentBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoritesFragment : Fragment(R.layout.favorites_fragment) {

    private var _binding: FavoritesFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: WeatherBroadcastsAdapter

    private val activityViewModel: MainViewModel by activityViewModels{
        MainViewModel.Factory
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FavoritesFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        adapter = WeatherBroadcastsAdapter { city ->

            activityViewModel.updateWeatherBroadcast(city)
            supportFragmentManager.commit {
                replace<WeatherFragment>(R.id.fragment_container_view)
            }
        }

        val dividerItemDecoration = DividerItemDecoration(this, RecyclerView.VERTICAL)
        dividerItemDecoration.setDrawable(resources.gerDrawable(R.drawable.divider_drawable))

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
            activityViewModel.favoritesUiState.collect { state ->
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
            activityViewModel.refreshWeather()

        }
    }

    private fun initDecoration() {
        
        
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}