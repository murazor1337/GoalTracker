package com.example.scheduler.ui

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.scheduler.R
import kotlinx.android.synthetic.main.fragment_calendar.view.*

class CalendarFragment : Fragment(R.layout.fragment_calendar) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.calendar_view.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val currentMonth = month + 1
            val currentDay = if (currentMonth < 10) {
                "$dayOfMonth.0$currentMonth.$year"
            }
            else {
                "$dayOfMonth.$currentMonth.$year"
            }
            findNavController().navigate(R.id.action_calendarFragment_to_dayFragment,
                bundleOf("dateArg" to currentDay))
        }
    }
}