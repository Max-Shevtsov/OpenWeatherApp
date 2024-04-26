class FavoritesFragment: Fragment(R.layout.favorites_fragment) {
    
    private val _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: WeatherBroadcastsAdapter

    override fun onCreateView(
        inflater: layoutInflater, 
        container: ViewGroup?,
        savedInstanceState: Bundle?,
        ): View? {
            _binding = ActivityMainBinding.inflate(inflater, container, false)
            val view = binding.root
            
            adapter = WeatherBroadcastsAdapter { cityId ->
                viewModel.deleteCityFromDb(cityId)
            }
            
            val recyclerView = binding.recyclerView
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this)

            initListeners()
            renderState()

            return view
        }

    override fun onViewCreated(view: View, sevedInstanceState: Bundle?) {
    
    }

    private fun renderState() {
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.uiState.collect { state ->
                adapter.submitList(state.allCity)
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
