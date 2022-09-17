package io.raveerocks.downloadmanager.ui.add

import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.raveerocks.downloadmanager.R
import io.raveerocks.downloadmanager.databinding.FragmentAddDownloadBinding

class AddDownloadFragment : BottomSheetDialogFragment() {

    private val fragmentViewModel: AddDownloadViewModel by lazy { ViewModelProvider(this)[AddDownloadViewModel::class.java] }
    private lateinit var binding: FragmentAddDownloadBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddDownloadBinding.inflate(layoutInflater, container, false)
        bind()
        setupObservers()
        return binding.root
    }

    private fun bind() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = fragmentViewModel
            downloadTitleEditText.setOnFocusChangeListener { _, focussed ->
                if (!focussed) {
                    binding.downloadTitleContainerLayout.helperText = validateTitle()
                }
            }
            downloadUrlEditText.setOnFocusChangeListener { _, focussed ->
                if (!focussed) {
                    binding.downloadUrlContainerLayout.helperText = validateURL()
                }
            }
            downloadButton.setOnClickListener {
                onAddDownload()
            }

            executePendingBindings()
        }
    }

    private fun setupObservers() {
        fragmentViewModel.downloadAddedEvent.observe(viewLifecycleOwner) {
            if (it) {
                onDownloadAdded()
                fragmentViewModel.downloadAddedDone()
            }
        }
        fragmentViewModel.downloadFailedEvent.observe(viewLifecycleOwner) {
            if (it) {
                onDownloadFailed()
                fragmentViewModel.downloadFailedDone()
            }
        }
    }

    private fun onAddDownload() {
        if (validateForm()) {
            fragmentViewModel.addDownload()
        }
    }

    private fun validateForm(): Boolean {
        return validateTitle().isEmpty() && validateURL().isEmpty()
    }

    private fun validateTitle(): String {
        val title = binding.downloadTitleEditText.text
        return if ((title == null) || title.isEmpty()) {
            getString(R.string.message_title_cannot_be_empty)
        } else ""
    }

    private fun validateURL(): String {
        val url = binding.downloadUrlEditText.text
        if (!Patterns.WEB_URL.matcher(url!!).matches()) {
            return getString(R.string.message_invalid_link)
        }
        val uri = Uri.parse(url.toString())
        val index = uri.path?.lastIndexOf('/') ?: -1
        if (index == -1 || uri.path?.substring(index + 1).isNullOrEmpty()) {
            return getString(R.string.label_invalid_resource)
        }
        return ""
    }

    private fun onDownloadAdded() {
        dismiss()
    }

    private fun onDownloadFailed() {
        binding.downloadTitleContainerLayout.helperText =
            getString(R.string.message_failed_adding_download)
    }

}