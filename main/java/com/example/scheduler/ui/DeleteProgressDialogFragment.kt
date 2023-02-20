package com.example.scheduler.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.scheduler.R
import com.example.scheduler.repository.Repository
import com.example.scheduler.view_models.DayInfoViewModelFactory
import com.example.scheduler.view_models.ProgressViewModel
import kotlinx.android.synthetic.main.dialog_fragment_delete_progress.*

class DeleteProgressDialogFragment : DialogFragment() {

    private lateinit var listener: EditListener
    private val viewModel: ProgressViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            DayInfoViewModelFactory(Repository.getInstance(requireContext()))
        )[ProgressViewModel::class.java]
    }

    interface EditListener {
        fun onDeleteButtonClicked(progressName: String? = null, save: Boolean)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as EditListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement EditListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setProgressNameListener()
        return inflater.inflate(R.layout.dialog_fragment_delete_progress, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupSpinner()
        button_delete_progress.setOnClickListener {
            val selectedProgress = if (spinner_progress.adapter.count != 0) {
                spinner_progress.selectedItem as String
            } else null
            val isSave = check_box_save.isChecked
            listener.onDeleteButtonClicked(selectedProgress, isSave)
            findNavController().navigate(R.id.action_deleteProgressDialogFragment_to_spheresFragment)
        }
        button_new_progress.setOnClickListener {
            findNavController().navigate(R.id.action_deleteProgressDialogFragment_to_createProgressDialogFragment)
        }
    }

    private fun setupSpinner() {
        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            viewModel.progress
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner_progress.adapter = it
        }
    }

    private fun setProgressNameListener() {
        setFragmentResultListener("progressNameResult") { _, bundle ->
            val progressName = bundle.getString("progressName")
            progressName?.let { viewModel.progress.add(0, it) }
            setupSpinner()
        }
    }
}