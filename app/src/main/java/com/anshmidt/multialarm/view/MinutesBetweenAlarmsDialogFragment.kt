package com.anshmidt.multialarm.view

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.anshmidt.multialarm.R
import com.anshmidt.multialarm.databinding.DialogIntervalBinding
import com.anshmidt.multialarm.viewmodel.MainViewModel

class MinutesBetweenAlarmsDialogFragment : DialogFragment(){

    companion object {
        val FRAGMENT_TAG = MinutesBetweenAlarmsDialogFragment::class.java.simpleName
    }

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val binding = DataBindingUtil.inflate<DialogIntervalBinding>(
                requireActivity().layoutInflater,
                R.layout.dialog_interval,
                null,
                false
        )

        initBinding(binding)

        val dialogView = binding.root

        initNumberPicker(dialogView)

        mainViewModel.onMinutesBetweenAlarmsDialogCreated()

        return buildDialog(dialogView)
    }

    private fun initBinding(binding: DialogIntervalBinding) {
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
    }

    private fun initNumberPicker(dialogView: View) {
        val numberPicker = dialogView.findViewById<NumberPicker>(R.id.numberpicker_intervaldialog)
        numberPicker.wrapSelectorWheel = false
        numberPicker.minValue = 0
        numberPicker.maxValue = mainViewModel.minutesBetweenAlarmsAllAvailableVariants.size - 1
        numberPicker.displayedValues = mainViewModel.minutesBetweenAlarmsAllAvailableVariants.map { it.toString() }.toTypedArray()
    }

    private fun buildDialog(dialogView: View): AlertDialog {
        val dialogBuilder = AlertDialog.Builder(requireContext())

        dialogBuilder.setView(dialogView)

        dialogBuilder.setPositiveButton(R.string.dialog_ok_button_name) {
            dialog, which -> mainViewModel.onOkButtonClickInMinutesBetweenAlarmsDialog()
        }

        dialogBuilder.setNegativeButton(R.string.dialog_cancel_button_name) {
            dialog, which -> mainViewModel.onCancelButtonClickInMinutesBetweenAlarmsDialog()
        }

        return dialogBuilder.create()
    }


}