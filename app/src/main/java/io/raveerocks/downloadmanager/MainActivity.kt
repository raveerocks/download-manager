package io.raveerocks.downloadmanager

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import io.raveerocks.downloadmanager.databinding.ActivityMainBinding
import io.raveerocks.downloadmanager.ui.add.AddDownloadFragment


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val activityViewModel: MainViewModel by lazy { ViewModelProvider(this)[MainViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        navController =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container_view)
                ?.findNavController()!!
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_dashboard,
                R.id.navigation_active,
                R.id.navigation_done,
                R.id.navigation_failed,
                R.id.navigation_settings
            )
        )
        bind()
        setupNavigation()
        setupObservers()
        setContentView(binding.root)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    private fun bind() {
        binding.apply {
            lifecycleOwner = this@MainActivity
            bottomNavView.setupWithNavController(navController)
            addFab.setOnClickListener { showDownloadFragment() }
        }
    }

    private fun setupNavigation() {
        setupActionBarWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            controlFABVisibility(
                destination
            )
        }
    }

    private fun setupObservers() {
        activityViewModel.eventShowFAB.observe(this, this::showFAB)
        activityViewModel.eventHideFAB.observe(this, this::hideFAB)
    }

    private fun controlFABVisibility(destination: NavDestination) {
        binding.addFab.visibility =
            if (destination.id == R.id.navigation_active || destination.id == R.id.navigation_done) {
                View.VISIBLE
            } else {
                View.GONE
            }
    }

    private fun hideFAB(hide: Boolean) {
        if (hide) {
            binding.addFab.visibility = View.GONE
            activityViewModel.hideFABDone()
        }
    }

    private fun showFAB(show: Boolean) {
        if (show) {
            binding.addFab.visibility = View.VISIBLE
            activityViewModel.showFABDone()
        }
    }

    private fun showDownloadFragment() {
        val dialog = AddDownloadFragment()
        dialog.show(supportFragmentManager, "AddDownloadFragment")
    }

}