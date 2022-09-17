package io.raveerocks.downloadmanager.ui.done

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import io.raveerocks.downloadmanager.databinding.FragmentDoneDownloadInfoBinding

class DoneDownloadInfoFragment : Fragment() {

    private lateinit var binding: FragmentDoneDownloadInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDoneDownloadInfoBinding.inflate(inflater, container, false)
        bind()
        return binding.root
    }

    private fun bind() {
        val detailFragmentArgs by navArgs<DoneDownloadInfoFragmentArgs>()
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            doneDownload = detailFragmentArgs.doneDownload
        }
    }


}