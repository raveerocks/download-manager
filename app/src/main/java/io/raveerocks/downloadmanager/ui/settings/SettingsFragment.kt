package io.raveerocks.downloadmanager.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.raveerocks.downloadmanager.MainActivity
import io.raveerocks.downloadmanager.R
import io.raveerocks.downloadmanager.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val fragmentViewModel: SettingsViewModel by lazy { ViewModelProvider(this)[SettingsViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        bind()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        fragmentViewModel.apply { loadPreferences() }
    }


    private fun bind() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = fragmentViewModel
            backupLimitSlider.setLabelFormatter { value -> "${value.toInt()} ${getString(R.string.label_days)}" }
            backupLimitSlider.addOnChangeListener { _, value, _ ->
                fragmentViewModel.setDashboardHistoryLimit(
                    value.toInt()
                )
            }
            saveButton.setOnClickListener {
                fragmentViewModel.savePreferences()
                Toast.makeText(context, R.string.message_relaunch, Toast.LENGTH_SHORT).show()
                startActivity(Intent(context, MainActivity::class.java)).apply {
                }
            }
            resetButton.setOnClickListener {
                fragmentViewModel.resetPreferences()
                Toast.makeText(context, R.string.message_relaunch, Toast.LENGTH_SHORT).show()
                startActivity(Intent(context, MainActivity::class.java))
            }
        }
    }
}