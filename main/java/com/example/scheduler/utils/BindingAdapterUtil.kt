package com.example.scheduler.utils

import android.widget.Spinner
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.example.scheduler.R

object BindingAdapterUtil {

    @JvmStatic
    @BindingAdapter("app:cardBackgroundColor")
    fun setCardBackgroundColor(cardView: CardView, completion: Int) {
        cardView.setBackgroundColor(
            when (completion) {
                in 0..50 -> cardView.resources.getColor(R.color.red)
                in 51..80 -> cardView.resources.getColor(R.color.yellow)
                else -> cardView.resources.getColor(R.color.green)
            }
        )
    }

    @JvmStatic
    @BindingAdapter("android:selectedItemPosition")
    fun setSelectedCategory(spinner: Spinner, category: String) {
        spinner.setSelection(
            when(category) {
                "Health" -> 0
                "Socialisation" -> 1
                "Career" -> 2
                "Education" -> 3
                else -> 4
            }
        )
    }
}