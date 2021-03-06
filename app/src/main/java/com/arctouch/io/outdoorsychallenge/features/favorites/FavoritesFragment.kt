package com.arctouch.io.outdoorsychallenge.features.favorites

import android.animation.LayoutTransition
import android.os.Bundle
import android.speech.RecognizerIntent.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.arctouch.io.outdoorsychallenge.R
import com.arctouch.io.outdoorsychallenge.connectivity.ErrorHandlingFragment
import com.arctouch.io.outdoorsychallenge.databinding.FragmentFavoritesBinding
import com.arctouch.io.outdoorsychallenge.features.main.MainViewModel
import com.arctouch.io.outdoorsychallenge.features.vehicleadapter.VehicleAdapter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : ErrorHandlingFragment() {

    override lateinit var binding: FragmentFavoritesBinding
    override val viewModel: FavoritesViewModel by viewModel()
    override val navController by lazy { findNavController() }
    private val sharedViewModel: MainViewModel by sharedViewModel()

    private lateinit var vehicleAdapter: VehicleAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentFavoritesBinding.inflate(inflater).apply {
        lifecycleOwner = viewLifecycleOwner
        viewModel = this@FavoritesFragment.viewModel

        binding = this
    }.root

    override fun doOnViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViews()
        observeEvents()
    }

    private fun setupViews() = with(binding) {
        favoritesResultsSrl.apply {
            setOnRefreshListener { this@FavoritesFragment.viewModel.onSwipeToRefresh() }
            setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary)
            layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)
        }

        vehicleAdapter = VehicleAdapter(viewLifecycleOwner)

        favoritesResultsRv.adapter = vehicleAdapter

    }

    private fun observeEvents() {
        viewModel.vehicles.observe(viewLifecycleOwner) {
            vehicleAdapter.submitList(it)
            binding.favoritesResultsSrl.isRefreshing = false
        }

        sharedViewModel.onFavoritesTabSelected.observe(viewLifecycleOwner) {
            viewModel.onTabSelected()
        }
    }

    companion object {

        fun newInstance() = FavoritesFragment()
    }
}
