package com.example.scheduler.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduler.databinding.ActivitiesListByCategoryItemBinding
import com.example.scheduler.model.Activity

class ActivitiesByCategoryAdapter(private val list: List<Activity>) :
    RecyclerView.Adapter<ActivitiesByCategoryAdapter.ActivityViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ActivitiesByCategoryAdapter.ActivityViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ActivitiesListByCategoryItemBinding.inflate(layoutInflater)
        return ActivityViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ActivitiesByCategoryAdapter.ActivityViewHolder,
        position: Int
    ) { holder.binding.activity = list[position] }

    override fun getItemCount(): Int = list.size

    inner class ActivityViewHolder(val binding: ActivitiesListByCategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}