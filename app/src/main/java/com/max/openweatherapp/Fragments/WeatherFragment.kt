import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.max.openweatherapp.MainViewModel
import com.max.openweatherapp.R
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
// перенести все из фрагмента во вьюмодель?
    private fun weatherTypeListener() {
        val weatherType = WeatherType()
        when (weatherUiState.city.cityWeatherType) {
            weatherType.CLEAR_SKY -> url = "https://openweathermap.org/img/wn/01d@2x.png",
            weatherType.FEW_CLOUDS -> url = "https://openweathermap.org/img/wn/02d@2x.png",
            weatherType.SCATTERED_CLOUDS -> url = "https://openweathermap.org/img/wn/03d@2x.png",
            weatherType.BROKEN_CLOUDS -> url = "https://openweathermap.org/img/wn/04d@2x.png",
            weatherType.SHOWER_RAIN -> url = "https://openweathermap.org/img/wn/09d@2x.png",
            weatherType.RAIN -> url = "https://openweathermap.org/img/wn/10d@2x.png",
            weatherType.THUNDERSTROM -> url = "https://openweathermap.org/img/wn/11d@2x.png",
            weatherType.SNOW -> url = "https://openweathermap.org/img/wn/13d@2x.png",
            weatherType.MIST -> url = "https://openweathermap.org/img/wn/50d@2x.png",
            weatherType.NO -> TODO() //не грузить ничего
        }
        //создать weatherTypeImageView
        getWeatherTypeImage(url, binding.weatherTypeImage)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}