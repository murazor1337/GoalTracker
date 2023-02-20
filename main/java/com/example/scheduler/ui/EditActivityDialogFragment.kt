package com.example.scheduler.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.example.scheduler.R
import com.example.scheduler.databinding.DialogFragmentEditActivityBinding
import com.example.scheduler.model.Activity
import com.example.scheduler.model.Category
import kotlinx.android.synthetic.main.dialog_fragment_edit_activity.view.*

class EditActivityDialogFragment : DialogFragment() {

    private lateinit var binding: DialogFragmentEditActivityBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val inflater = requireActivity().layoutInflater
            binding = DataBindingUtil.inflate(inflater,
                R.layout.dialog_fragment_edit_activity, null, false)
            AlertDialog.Builder(it).apply {
                setView(binding.root)
                setPositiveButton("Save") { _, _ ->
                    setActivityResult()
                }
                setNegativeButton("Cancel") { _, _ ->
                    dialog?.cancel()
                }
            }.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val activityArg = arguments?.getSerializable("activityArg") as Activity?
        binding.activity = activityArg
        binding.root.button_update_day.setOnClickListener {
            setActivityUpdateResult()
        }
        setupSpinner()
        setupSeekBar()
    }

    private fun setupSpinner() {
        val spinner = binding.root.spinner_category
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.categories_array, android.R.layout.simple_spinner_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = it
        }
    }

    private fun setupSeekBar() {
        val textViewCompletion = binding.root.text_view_completionLabel
        val seekBarCompletion = binding.root.seekbar_completion

        seekBarCompletion.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    textViewCompletion.text = progress.toString()
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) { }
                override fun onStopTrackingTouch(seekBar: SeekBar?) { }
            }
        )
    }

    private fun createActivity(activityId: Long = 0) = Activity(
        id = activityId,
        description = binding.root.edit_text_name.text.toString(),
        category = when(binding.root.spinner_category.selectedItemPosition) {
            0 -> Category.Health
            1 -> Category.Socialisation
            2 -> Category.Career
            3 -> Category.Education
            else -> Category.SelfDevelopment
        },
        date = arguments?.getString("dayArg") as String,
        points = binding.root.edit_text_points.text.toString().toInt(),
        completion = binding.root.seekbar_completion.progress,
        isPlanned = binding.root.check_box_planned.isChecked
    )

    private fun setActivityResult() {
        val activityResult = createActivity()
        setFragmentResult("activityResult",
            bundleOf("activityKey" to activityResult))
    }

    private fun setActivityUpdateResult() {
        val updatedActivity = createActivity(binding.activity!!.id)
        setFragmentResult("activityUpdateResult",
            bundleOf("activityUpdKey" to updatedActivity))
        findNavController().navigate(R.id.action_editActivityDialogFragment_to_dayFragment,
        args = bundleOf("dateArg" to updatedActivity.date))
    }
}