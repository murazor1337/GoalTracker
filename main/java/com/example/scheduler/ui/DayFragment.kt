package com.example.scheduler.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduler.R
import com.example.scheduler.adapters.ActivitiesAdapter
import com.example.scheduler.model.Activity
import com.example.scheduler.repository.Repository
import com.example.scheduler.view_models.DayInfoViewModel
import com.example.scheduler.view_models.DayInfoViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_day.*
import kotlinx.android.synthetic.main.fragment_day.view.*

class DayFragment : Fragment() {

    private lateinit var viewModel: DayInfoViewModel
    private lateinit var listAdapter: ActivitiesAdapter
    private lateinit var date: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity(),
            DayInfoViewModelFactory(Repository.getInstance(requireContext())))[DayInfoViewModel::class.java]
        date = arguments?.getString("dateArg").toString()
        getDayWithActivities(date)
        return inflater.inflate(R.layout.fragment_day, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupFragmentResultListener()
        listAdapter = ActivitiesAdapter()
        view.recycler_view_activities.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = listAdapter
        }
        viewModel.dayActivities.observe(viewLifecycleOwner) { dayWithActivities ->
            try {
                listAdapter.listDiffer.submitList(dayWithActivities.first().activities)
                viewModel.isDayAdded = true
                if (dayWithActivities.first().day.added) {
                    view.button_save_day.isGone = true
                } else {
                    view.button_save_day.isGone = false
                    view.button_delete_day.isGone = true
                }
            } catch (e: NoSuchElementException) {
                listAdapter.listDiffer.submitList(emptyList())
                viewModel.isDayAdded = false
                view.button_save_day.isGone = true
                view.button_delete_day.isGone = true
            } finally {
                view.text_view_selected_day.text = date
            }
        }
        view.floating_action_button.setOnClickListener {
            launchCreateActivityDialog()
        }
        view.button_save_day.setOnClickListener {
            saveDay()
        }
        view.button_delete_day.setOnClickListener {
            deleteActivitiesForDay()
        }
        view.button_back.setOnClickListener {
            navigateBack()
        }
        setupDeleteTouchHelper()
    }

    private fun getDayWithActivities(date: String) {
        viewModel.getDayWithActivities(date)
    }

    private fun launchCreateActivityDialog() {
        findNavController().navigate(R.id.action_dayFragment_to_editActivityDialogFragment,
            args = bundleOf("dayArg" to date))
    }

    private fun saveDay() {
        viewModel.updateSpheresInfo(listAdapter.listDiffer.currentList, true)
        findNavController().navigate(R.id.action_dayFragment_to_spheresFragment,
        args = bundleOf("isSpheresListWasUpdated" to true))
    }

    private fun navigateBack() {
        findNavController().navigate(R.id.action_dayFragment_to_calendarFragment)
    }

    private fun deleteActivitiesForDay() {
        viewModel.updateSpheresInfo(listAdapter.listDiffer.currentList, false)
        viewModel.deleteAllActivitiesForDay(date)
        findNavController().navigate(R.id.action_dayFragment_to_spheresFragment,
            args = bundleOf("isSpheresListWasUpdated" to true))
    }

    private fun setupFragmentResultListener() {
        setFragmentResultListener("activityResult") { _, bundle ->
            val createdActivity = bundle.getSerializable("activityKey") as Activity
            if (viewModel.validateActivityInfo(createdActivity)) {
                viewModel.insertActivity(createdActivity)
            } else showMessage("Not allowed info was entered in activity creation.")
        }
        setFragmentResultListener("activityUpdateResult") {_, bundle ->
            val updatedActivity = bundle.getSerializable("activityUpdKey") as Activity
            if (viewModel.validateActivityInfo(updatedActivity)) {
                viewModel.updateActivity(updatedActivity)
                showMessage("Activity was updated successfully.", R.color.teal_700)
            } else showMessage("Not allowed info was entered in activity update.")
        }
    }

    private fun showMessage(message: String, color: Int = R.color.red) = view?.let {
        Snackbar.make(
            it,
            message,
            Snackbar.LENGTH_LONG).
        setBackgroundTint(resources.getColor(color)).show()
    }

    private fun setupDeleteTouchHelper() {
        val touchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val activitiesList = listAdapter.listDiffer.currentList
                val activity = activitiesList[position]
                val deleteDay = activitiesList.size == 1
                viewModel.deleteActivity(activity, deleteDay)
                view?.let {
                    Snackbar.make(it, "Activity was deleted", Snackbar.LENGTH_LONG).apply {
                        setAction("Undo") {
                            viewModel.insertActivity(activity)
                        }
                        setBackgroundTint(resources.getColor(R.color.white))
                    }.show()
                }
            }
        }
        ItemTouchHelper(touchHelperCallback).attachToRecyclerView(recycler_view_activities)
    }
}