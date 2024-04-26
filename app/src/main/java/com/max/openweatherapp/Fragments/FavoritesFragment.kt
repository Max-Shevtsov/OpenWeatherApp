class FavoritesFragment: Fragment(R.layout.favorites_fragment) {
    
    private val _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: layoutInflater, 
        container: ViewGroup?,
        savedInstanceState: Bundle?,
        ): View? {
            _binding = ActivityMainBinding.inflate(inflater, container, false)
            val view = binding.root
            return view
        }

    override fun onViewCreated(view: View, sevedInstanceState: Bundle?) {
    
    }

    overrid fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
