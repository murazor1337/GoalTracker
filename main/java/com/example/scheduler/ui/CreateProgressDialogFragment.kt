package com.example.scheduler.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.example.scheduler.R
import kotlinx.android.synthetic.main.dialog_fragment_create_progress.*

class CreateProgressDialogFragment : DialogFragment(R.layout.dialog_fragment_create_progress) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        button_ok.isEnabled = false
        edit_text_progress_name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun afterTextChanged(s: Editable?) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                button_ok.isEnabled = s.toString().isNotBlank() and (s.toString().length <= 8)
            }
        })
        button_ok.setOnClickListener {
            setProgressNameResult()
        }
        button_cancel.setOnClickListener {
            findNavController().navigate(
                R.id.action_createProgressDialogFragment_to_deleteProgressDialogFragment
            )
        }
    }

    private fun setProgressNameResult() {
        val enteredName = edit_text_progress_name.text.toString()
        setFragmentResult("progressNameResult",
            bundleOf("progressName" to enteredName)
        )
        findNavController().navigate(
            R.id.action_createProgressDialogFragment_to_deleteProgressDialogFragment
        )
    }
}