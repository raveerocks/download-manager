package io.raveerocks.downloadmanager.ui.done

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.raveerocks.downloadmanager.MainViewModel
import io.raveerocks.downloadmanager.R
import io.raveerocks.downloadmanager.core.models.BaseModel.ActionListener
import io.raveerocks.downloadmanager.core.models.DoneDownload
import io.raveerocks.downloadmanager.core.types.SortFieldType
import io.raveerocks.downloadmanager.databinding.FragmentDoneBinding

class DoneFragment : Fragment() {

    private lateinit var binding: FragmentDoneBinding
    private val fragmentViewModel: DoneViewModel by lazy { ViewModelProvider(this)[DoneViewModel::class.java] }
    private val activityViewModel: MainViewModel by lazy { ViewModelProvider(requireActivity())[MainViewModel::class.java] }
    private val itemAdapter: DoneDownloadItemAdapter by lazy {
        DoneDownloadItemAdapter(
            ActionListener(this::onOpenDownload),
            ActionListener(this::onViewDownloadInfo),
            ActionListener(this::onDeleteDownload),
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDoneBinding.inflate(inflater, container, false)
        bind()
        setupObservers()
        setupMenu()
        return binding.root
    }

    private fun bind() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = fragmentViewModel
            doneRecyclerView.apply {
                lifecycleOwner = viewLifecycleOwner
                adapter = itemAdapter
                addOnScrollListener(onDownloadsScrolled())
            }
            doneRecyclerViewContainerLayout.setOnRefreshListener { onContainerRefresh() }
            executePendingBindings()
        }
    }

    private fun setupObservers() {
        fragmentViewModel.doneDownloads.observe(viewLifecycleOwner, this::onDataChange)
        fragmentViewModel.downloadOpenFailedEvent.observe(viewLifecycleOwner) {
            if (it) {
                onDownloadOpenFailed()
                fragmentViewModel.onDownloadOpenFailedDone()
            }
        }
    }

    private fun setupMenu() {
        requireActivity().addMenuProvider(
            getMenuProvider(),
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
    }

    private fun onOpenDownload(doneDownload: DoneDownload) {
        fragmentViewModel.openDownload(doneDownload)
    }

    private fun onDeleteDownload(doneDownload: DoneDownload) {
        fragmentViewModel.deleteDownload(doneDownload)
    }

    private fun onViewDownloadInfo(doneDownload: DoneDownload) {
        binding.root.findNavController()
            .navigate(
                DoneFragmentDirections.actionNavigationDoneToDoneDownloadInfoFragment(
                    doneDownload
                )
            )
    }

    private fun onDataChange(doneDownloads: List<DoneDownload>) {
        doneDownloads.let {
            if (it.isEmpty()) {
                binding.noDownloads.visibility = View.VISIBLE
            } else {
                binding.noDownloads.visibility = View.GONE
            }
            itemAdapter.submitList(it)
        }
    }

    private fun onDownloadOpenFailed() {
        Toast.makeText(
            context,
            getString(R.string.message_file_open_failed),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun onContainerRefresh() {
        fragmentViewModel.refreshDownloads()
        onContainerRefreshed()
    }

    private fun getMenuProvider(): MenuProvider {
        val menuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.done_sort_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.sort_by_title -> {
                        fragmentViewModel.sortDownloads(SortFieldType.TITLE_ASC)
                    }
                    R.id.sort_by_domain -> {
                        fragmentViewModel.sortDownloads(SortFieldType.DOMAIN_ASC)
                    }
                    R.id.sort_by_size -> {
                        fragmentViewModel.sortDownloads(SortFieldType.SIZE_ASC)
                    }
                    R.id.sort_by_date -> {
                        fragmentViewModel.sortDownloads(SortFieldType.COMPLETED_AT_ASC)
                    }
                    else -> return false
                }
                return true
            }
        }
        return menuProvider
    }

    private fun onContainerRefreshed() {
        binding.doneRecyclerViewContainerLayout.isRefreshing = false
    }

    private fun onDownloadsScrolled(): RecyclerView.OnScrollListener {
        val listener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    activityViewModel.hideFAB()
                    if ((binding.doneRecyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition() == 0) {
                        binding.chipGroupContainerMotionLayout.transitionToEnd()
                    }

                } else {
                    activityViewModel.showFAB()
                    if ((binding.doneRecyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition() == 0) {
                        binding.chipGroupContainerMotionLayout.transitionToStart()
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        }
        return listener
    }

}