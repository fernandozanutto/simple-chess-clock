package com.fzanutto.simplechessclock

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.fzanutto.simplechessclock.databinding.TimePickerDialogBinding
import java.lang.Exception

class TimePickerDialog: DialogFragment() {

    private lateinit var listener: TimePickerDialogListener

    interface TimePickerDialogListener {
        fun onPositiveClick(minutes: Int, seconds: Int)
    }

    lateinit var binding: TimePickerDialogBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = TimePickerDialogBinding.inflate(layoutInflater)

        binding.minutesPicker.minValue = 0
        binding.minutesPicker.maxValue = 120
        binding.minutesPicker.value = 3

        binding.secondsPicker.minValue = 0
        binding.secondsPicker.maxValue = 59

        return activity?.let {
            val builder = AlertDialog.Builder(it)

            builder.setView(binding.root)
                .setPositiveButton("Set Timer") { _, _ ->
                    listener.onPositiveClick(binding.minutesPicker.value, binding.secondsPicker.value)
                }
                .setNegativeButton("Cancel") { _, _ -> }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = context as TimePickerDialogListener
        } catch (e: Exception) {
            throw ClassCastException(("$context must implement NoticeDialogListener"))
        }
    }
}