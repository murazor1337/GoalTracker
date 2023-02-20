package com.example.scheduler.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduler.R
import com.example.scheduler.model.Activity
import kotlinx.android.synthetic.main.activities_list_item.view.*

class ActivitiesAdapter : RecyclerView.Adapter<ActivitiesAdapter.ActivityViewHolder>() {

    val listDiffer: AsyncListDiffer<Activity> = AsyncListDiffer(this,
        object : DiffUtil.ItemCallback<Activity>() {
            override fun areItemsTheSame(oldItem: Activity, newItem: Activity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Activity, newItem: Activity): Boolean {
                return oldItem == newItem
            }
        })

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ActivitiesAdapter.ActivityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activities_list_item,
        parent, false)
        return ActivityViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActivitiesAdapter.ActivityViewHolder, position: Int) {
        val activity = listDiffer.currentList[position]
        holder.bindViewHolder(activity)
    }

    override fun getItemCount(): Int = listDiffer.currentList.size

    inner class ActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindViewHolder(activity: Activity) {
            itemView.apply {
                text_view_name.text = activity.description
                text_view_category.text = activity.category.categoryName
                text_view_points.text = activity.points.toString()
                val completion = activity.completion
                check_box_planned.isChecked = activity.isPlanned
                val color = if(check_box_planned.isChecked) {
                    when(completion) {
                        in 0..50 -> resources.getColor(R.color.red)
                        in 51..80 -> resources.getColor(R.color.yellow)
                        else -> resources.getColor(R.color.green)
                    }
                } else resources.getColor(R.color.grey)
                text_view_completion.text = completion.toString()
                setBackgroundColor(color)
                setOnClickListener {
                    findNavController().navigate(
                        R.id.action_dayFragment_to_editActivityDialogFragment,
                        args = bundleOf("activityArg" to activity,
                            "dayArg" to activity.date)
                    )
                }
            }
        }
    }
}