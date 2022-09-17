package io.raveerocks.downloadmanager.ui.active

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import io.raveerocks.downloadmanager.MainViewModel
import io.raveerocks.downloadmanager.R
import io.raveerocks.downloadmanager.core.models.ActiveDownload
import io.raveerocks.downloadmanager.core.models.BaseModel.ActionListener
import io.raveerocks.downloadmanager.databinding.FragmentActiveBinding

class ActiveFragment : Fragment() {

    private lateinit var binding: FragmentActiveBinding
    private val fragmentViewModel: ActiveViewModel by lazy { ViewModelProvider(this)[ActiveViewModel::class.java] }
    private val mainViewModel: MainViewModel by lazy { ViewModelProvider(requireActivity())[MainViewModel::class.java] }
    private val itemAdapter: ActiveDownloadItemAdapter by lazy {
        ActiveDownloadItemAdapter(
            ActionListener(this::onCancelDownload)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentActiveBinding.inflate(inflater, container, false)
        bind()
        setupObservers()
        return binding.root
    }

    private fun bind() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            activeRecyclerView.apply {
                lifecycleOwner = viewLifecycleOwner
                adapter = itemAdapter
                addOnScrollListener(onDownloadsScrolled())
            }
            activeRecyclerViewContainerLayout.setOnRefreshListener { onRefreshContainer() }
            executePendingBindings()
        }
    }

    private fun setupObservers() {
        fragmentViewModel.activeDownloads.observe(viewLifecycleOwner, this::onDataChange)
        fragmentViewModel.downloadCanceledEvent.observe(viewLifecycleOwner) {
            if (it) {
                onDownloadCanceled()
                fragmentViewModel.downloadCanceledDone()
            }
        }
    }

    private fun onDataChange(doneDownloads: List<ActiveDownload>) {
        doneDownloads.let {
            if (it.isEmpty()) {
                binding.noDownloads.visibility = View.VISIBLE
            } else {
                binding.noDownloads.visibility = View.GONE
            }
            itemAdapter.submitList(it)
        }
    }

    private fun onCancelDownload(activeDownload: ActiveDownload) {
        fragmentViewModel.cancelDownload(activeDownload)
    }

    private fun onRefreshContainer() {
        fragmentViewModel.refreshDownloads()
        onContainerRefreshed()
    }

    private fun onDownloadCanceled() {
        Toast.makeText(
            context,
            getString(R.string.message_download_canceled),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun onContainerRefreshed() {
        binding.activeRecyclerViewContainerLayout.isRefreshing = false
    }

    private fun onDownloadsScrolled(): RecyclerView.OnScrollListener {
        val listener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    mainViewModel.hideFAB()
                } else {
                    mainViewModel.showFAB()
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        }
        return listener
    }
}