package com.example.scheduler.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scheduler.R
import com.example.scheduler.adapters.SpheresAdapter
import com.example.scheduler.view_models.SpheresViewModel
import kotlinx.android.synthetic.main.fragment_spheres.*
import kotlinx.android.synthetic.main.fragment_spheres.view.*

class SpheresFragment : Fragment() {

    private var isFabsVisible = false
    private val viewModel: SpheresViewModel by lazy {
        ViewModelProvider(requireActivity())[SpheresViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.getBoolean("isSpheresListWasUpdated")?.also {
            viewModel.isSpheresWasUpdated = it
        }
        return inflater.inflate(R.layout.fragment_spheres, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.recycler_view_spheres
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = SpheresAdapter(viewModel.spheresList)
        isFabsVisible = savedInstanceState?.getBoolean("isFabsVisible") ?: false
        changeVisibility(isFabsVisible)
        fab_extended.setOnClickListener {
            changeVisibility(!isFabsVisible)
        }
        fab_restore_progress.setOnClickListener {

        }
        fab_delete_progress.setOnClickListener {
            findNavController().navigate(R.id.action_spheresFragment_to_deleteProgressDialogFragment)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("isFabsVisible", isFabsVisible)
    }

    private fun changeVisibility(visibilityState: Boolean) {
        isFabsVisible = visibilityState
        if (!isFabsVisible) {
            fab_extended.shrink()
            fab_delete_progress.hide()
            fab_restore_progress.hide()
        } else {
            fab_extended.extend()
            fab_delete_progress.show()
            fab_restore_progress.show()
        }
    }
}