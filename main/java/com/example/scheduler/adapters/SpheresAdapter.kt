package com.example.scheduler.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduler.R
import com.example.scheduler.model.Category
import com.example.scheduler.model.Sphere

class SpheresAdapter(private var spheres: List<Sphere>) : RecyclerView.Adapter<SpheresAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpheresAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.spheres_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SpheresAdapter.ViewHolder, position: Int) {
        val listItem = spheres[position]
        holder.bindView(listItem)
    }

    override fun getItemCount() = spheres.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val sphereName = itemView.findViewById<TextView>(R.id.text_view_sphere_name)
        val sphereProgress = itemView.findViewById<TextView>(R.id.text_view_sphere_level_progress_value)
        val sphereProgressBar = itemView.findViewById<ProgressBar>(R.id.progress_bar)
        val sphereLevel = itemView.findViewById<TextView>(R.id.text_view_sphere_level_value)
        val sphereImage = itemView.findViewById<ImageView>(R.id.image_view_sphere)

        fun bindView(sphere: Sphere) {
            sphereName.text = sphere.name
            sphereProgress.text = sphere.progress.toString()
            sphereProgressBar.progress = sphere.progress
            sphereLevel.text = sphere.level.toString()
            sphereImage.setImageDrawable(sphere.image)
            itemView.setOnClickListener {
                it.findNavController().navigate(
                    R.id.action_spheresFragment_to_activitiesByCategoryFragment,
                    args = bundleOf("categoryKey" to when(sphere.name) {
                        "Health" -> Category.Health
                        "Socialisation" -> Category.Socialisation
                        "Career" -> Category.Career
                        "Education" -> Category.Education
                        else -> Category.SelfDevelopment
                    })
                )
            }
        }
    }

}