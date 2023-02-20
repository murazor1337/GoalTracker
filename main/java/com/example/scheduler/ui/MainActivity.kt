package com.example.scheduler.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.scheduler.R
import com.example.scheduler.view_models.SpheresViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), DeleteProgressDialogFragment.EditListener {

    private val viewModel: SpheresViewModel by lazy {
        ViewModelProvider(this)[SpheresViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val hostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = hostFragment.navController
        val bottomMenu = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomMenu.setupWithNavController(navController)

        bottomMenu.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.item_calendar -> {
                    navController.navigate(R.id.action_spheresFragment_to_calendarFragment)
                    true
                }
                R.id.item_progress -> {
                    navController.navigate(R.id.action_spheresFragment_to_progressFragment)
                    true
                }
                else -> false
            }
        }
    }

    override fun onStop() {
        viewModel.saveList()
        super.onStop()
    }

    override fun onDeleteButtonClicked(progressName: String?, save: Boolean) {
        viewModel.deleteProgress(progressName, save)
    }
}