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



class WeatherFragment: Fragment(R.layout.weather_fragment) {
    
    private var _binding: WeatherFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels{
        MainViewModelFactory(MainViewModel.Factory)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
        ): View? {
            _binding = WeatherFragmentBinding.inflate(inflater, container, false)
            val view = binding.root
            
            initListeners()
            //renderState()

            return view
        }

    override fun onViewCreated(view: View, sevedInstanceState: Bundle?) {
    
    }

//    private fun renderState() {
//        lifecycleScope.launch(Dispatchers.Main) {
//            viewModel.uiState.collect { state ->
//                //добавить поля для state полный прогноз погоды
//
//            }
//        }
//    }

    private fun initListeners() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}