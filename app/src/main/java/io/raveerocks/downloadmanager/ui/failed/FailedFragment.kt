package io.raveerocks.downloadmanager.ui.failed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.raveerocks.downloadmanager.R
import io.raveerocks.downloadmanager.core.models.BaseModel
import io.raveerocks.downloadmanager.core.models.FailedDownload
import io.raveerocks.downloadmanager.databinding.FragmentFailedBinding

class FailedFragment : Fragment() {

    private lateinit var binding: FragmentFailedBinding
    private val fragmentViewModel: FailedViewModel by lazy { ViewModelProvider(this)[FailedViewModel::class.java] }
    private val itemAdapter: FailedDownloadItemAdapter by lazy {
        FailedDownloadItemAdapter(
            BaseModel.ActionListener(this::onRetryDownload),
            BaseModel.ActionListener(this::onDeleteDownload),
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFailedBinding.inflate(inflater, container, false)
        bind()
        setupObservers()
        return binding.root
    }

    private fun bind() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            failedRecyclerView.apply {
                lifecycleOwner = viewLifecycleOwner
                adapter = itemAdapter
            }
            failedRecyclerViewContainerLayout.setOnRefreshListener { onContainerRefresh() }
            executePendingBindings()
        }
    }

    private fun setupObservers() {
        fragmentViewModel.failedDownloads.observe(viewLifecycleOwner, this::onDataChange)
        fragmentViewModel.downloadRetriedEvent.observe(viewLifecycleOwner) {
            if (it) {
                onDownloadRetried()
                fragmentViewModel.downloadRetriedDone()
            }
        }
    }

    private fun onDataChange(doneDownloads: List<FailedDownload>) {
        doneDownloads.let {
            if (it.isEmpty()) {
                binding.noDownloads.visibility = View.VISIBLE
            } else {
                binding.noDownloads.visibility = View.GONE
            }
            itemAdapter.submitList(it)
        }
    }

    private fun onRetryDownload(failedDownload: FailedDownload) {
        fragmentViewModel.retryDownload(failedDownload)
    }

    private fun onDeleteDownload(failedDownload: FailedDownload) {
        fragmentViewModel.deleteDownload(failedDownload)
    }

    private fun onContainerRefresh() {
        fragmentViewModel.refreshDownloads()
        binding.failedRecyclerViewContainerLayout.isRefreshing = false
    }

    private fun onDownloadRetried() {
        Toast.makeText(
            context,
            getString(R.string.message_retrying_download),
            Toast.LENGTH_SHORT
        ).show()
    }

}