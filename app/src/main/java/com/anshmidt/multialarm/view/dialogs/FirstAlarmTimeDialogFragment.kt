package com.anshmidt.multialarm.view.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.TimePicker
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.anshmidt.multialarm.R
import com.anshmidt.multialarm.databinding.DialogFirstAlarmTimeBinding
import com.anshmidt.multialarm.viewmodel.FirstAlarmTimeViewModel

class FirstAlarmTimeDialogFragment : DialogFragment() {

    companion object {
        val FRAGMENT_TAG = FirstAlarmTimeDialogFragment::class.java.simpleName
    }

    private val viewModel: FirstAlarmTimeViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        requireContext().getTheme().applyStyle(R.style.FirstAlarmTimeDialogTheme, true)

        val binding = DataBindingUtil.inflate<DialogFirstAlarmTimeBinding>(
                requireActivity().layoutInflater,
                R.layout.dialog_first_alarm_time,
                null,
                false
        )

        initBinding(binding)

        val dialogView = binding.root

        return buildDialog(dialogView)
    }

    private fun initBinding(binding: DialogFirstAlarmTimeBinding) {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun configureTimePicker(dialogView: View) {
        val timePicker: TimePicker = dialogView.findViewById(R.id.timepicker_firstalarmdialog)
        timePicker.setIs24HourView(true)
    }

    private fun buildDialog(dialogView: View): AlertDialog {
        val dialogBuilder = AlertDialog.Builder(requireContext(), R.style.DialogStyle)

        dialogBuilder.setView(dialogView)

        dialogBuilder.setPositiveButton(R.string.dialog_ok_button_name) {
            dialog, which -> viewModel.onOkButtonClickInFirstAlarmDialog()
        }

        dialogBuilder.setNegativeButton(R.string.dialog_cancel_button_name) {
            dialog, which -> viewModel.onCancelButtonClickInFirstAlarmDialog()
        }

        configureTimePicker(dialogView)

        return dialogBuilder.create()
    }


}