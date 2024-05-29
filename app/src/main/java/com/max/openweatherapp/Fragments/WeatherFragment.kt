import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.max.openweatherapp.MainViewModel
import com.max.openweatherapp.R
import com.max.openweatherapp.UI.WeatherType
import com.max.openweatherapp.databinding.WeatherFragmentBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class WeatherFragment : Fragment(R.layout.weather_fragment) {

    private var _binding: WeatherFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels {
        MainViewModel.Factory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = WeatherFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        initListeners()
        renderState()
        
        return view
    }

    override fun onViewCreated(view: View, sevedInstanceState: Bundle?) {

    }

    private fun renderState() {
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.weatherUiState.collect { state ->
                binding.cityName.text = state.city?.cityName
                binding.cityTemp.text = state.city?.cityTemp
                binding.cityWindSpeed.text = state.city?.cityWindSpeed
                binding.starButton.isChecked = state.city?.isStarred ?: false
                loadWeatherTypePicture(
                    url = viewModel.weatherTypeListener(state.city?.weatherType),
                    uiItem = binding.weatherType
                )
            }
        }
    }

    private fun initListeners() {

        binding.starButton.setOnClickListener {
            if (binding.starButton.isChecked) {
                viewModel.putCityIntoFavorites()
            }else {
                viewModel.deleteCityFromFavorites()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}