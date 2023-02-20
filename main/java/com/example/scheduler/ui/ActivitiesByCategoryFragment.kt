package com.example.scheduler.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scheduler.R
import com.example.scheduler.adapters.ActivitiesByCategoryAdapter
import com.example.scheduler.model.Category
import com.example.scheduler.repository.Repository
import com.example.scheduler.view_models.ActivitiesByCategoryViewModel
import com.example.scheduler.view_models.DayInfoViewModelFactory
import kotlinx.android.synthetic.main.fragment_activities_by_category.view.*

class ActivitiesByCategoryFragment : Fragment() {

    private val viewModel: ActivitiesByCategoryViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            DayInfoViewModelFactory(Repository.getInstance(requireContext()))
        )[ActivitiesByCategoryViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val activityCategory = arguments?.getSerializable("categoryKey") as Category
        viewModel.getActivitiesByCategory(activityCategory)
        val view = inflater.inflate(R.layout.fragment_activities_by_category, container, false)
        val categoryText = view.text_view_activity_category.text.toString()
        view.text_view_activity_category.text = categoryText + activityCategory.categoryName
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.recycler_view_activities_by_category
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        viewModel.activitiesList.observe(viewLifecycleOwner) { activities ->
            recyclerView.adapter = ActivitiesByCategoryAdapter(activities)
        }
    }
}