package io.raveerocks.downloadmanager.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.raveerocks.downloadmanager.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentDashboardBinding
    private val fragmentViewModel: DashboardViewModel by lazy { ViewModelProvider(this)[DashboardViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        bind()
        return binding.root
    }

    private fun bind() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = fragmentViewModel
            executePendingBindings()
        }
    }

}