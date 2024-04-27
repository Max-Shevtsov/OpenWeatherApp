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
        MainViewModelFactory(MainViewModel.Factory)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FavoritesFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        adapter = WeatherBroadcastsAdapter { cityId ->
            activityViewModel.deleteCityFromDb(cityId)
        }

        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        initListeners()
        renderState()

        return view
    }

    override fun onViewCreated(view: View, sevedInstanceState: Bundle?) {

    }

    private fun renderState() {
        lifecycleScope.launch(Dispatchers.Main) {
            activityViewModel.uiState.collect { state ->
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
            activityViewModel.updateWeatherBroadcast()

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
