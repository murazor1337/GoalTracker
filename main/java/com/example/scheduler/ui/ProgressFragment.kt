package com.example.scheduler.ui

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.scheduler.R
import com.example.scheduler.model.Category
import com.example.scheduler.repository.Repository
import com.example.scheduler.view_models.DayInfoViewModelFactory
import com.example.scheduler.view_models.ShowProgressViewModel
import kotlinx.android.synthetic.main.fragment_progress.*
import kotlinx.android.synthetic.main.fragment_progress.view.*

class ProgressFragment : Fragment(R.layout.fragment_progress) {

    private val viewModel: ShowProgressViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            DayInfoViewModelFactory(Repository.getInstance(requireContext()))
        )[ShowProgressViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val graphView = view.graph_view
        viewModel.daysWithPoints.observe(viewLifecycleOwner) { daysList ->
            daysList?.let { list ->
                list.sortedWith { firstDay, secondDay ->
                    val firstDate = firstDay.date.split(".")
                    val secondDate = secondDay.date.split(".")
                    when {
                        (firstDate[1].toInt() < secondDate[1].toInt()) -> -1
                        (firstDate[0].toInt() < secondDate[0].toInt()) -> -1
                        else -> 0
                    }
                }.also { graphView.setData(it) }
            }
        }
        setupSpinner()
    }

    private fun setupSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(), R.array.categories_progress_array, android.R.layout.simple_spinner_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner_category_progress.adapter = it
        }
        spinner_category_progress.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedCategory: Category? = when(parent?.getItemAtPosition(position)) {
                    "Health" -> Category.Health
                    "Socialisation" -> Category.Socialisation
                    "Career" -> Category.Career
                    "Education" -> Category.Education
                    "Self development" -> Category.SelfDevelopment
                    else -> null
                }
                viewModel.calculateProgress(selectedCategory)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
    }
}