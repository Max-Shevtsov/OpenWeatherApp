class WeatherFragment: Fragment(R.layout.weather_fragment) {
    
    private val _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: layoutInflater, 
        container: ViewGroup?,
        savedInstanceState: Bundle?,
        ): View? {
            _binding = ActivityMainBinding.inflate(inflater, container, false)
            val view = binding.root
            
            initListeners()
            renderState()

            return view
        }

    override fun onViewCreated(view: View, sevedInstanceState: Bundle?) {
    
    }

    private fun renderState() {
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.uiState.collect { state ->
                //добавить поля для state полный прогноз погоды
                if (!state.isLoading)
                    binding.swipeRefresh.isRefreshing = false
                if(state.errorMessage? != null)run {
                    val toast = Toast.makeText(context, state.errorMessage,).show()
                }
            }
        }
    }

    private fun initListeners() {
        binding.swipeRefresh.setOnRefreshListener {
            Log.e("!!!", "onRefresh called from SwipeRefreshLayout")
            viewModel.updateWeatherBroadcast() 
            
        }
    }

    overrid fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}