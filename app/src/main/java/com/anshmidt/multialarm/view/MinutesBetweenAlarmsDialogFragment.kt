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
import com.anshmidt.multialarm.view.helpers.NumberPickerHelper
import com.anshmidt.multialarm.viewmodel.MinutesBetweenAlarmsViewModel

class MinutesBetweenAlarmsDialogFragment : DialogFragment(){

    companion object {
        val FRAGMENT_TAG = MinutesBetweenAlarmsDialogFragment::class.java.simpleName
    }

    private val minutesBetweenAlarmsViewModel: MinutesBetweenAlarmsViewModel by activityViewModels()

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

        return buildDialog(dialogView)
    }

    private fun initBinding(binding: DialogIntervalBinding) {
        binding.lifecycleOwner = this
        binding.minutesBetweenAlarmsViewModel = minutesBetweenAlarmsViewModel
    }

    private fun initNumberPicker(dialogView: View) {
        val numberPicker = dialogView.findViewById<NumberPicker>(R.id.numberpicker_intervaldialog)
        val numberPickerHelper = NumberPickerHelper()
        val numberPickerData = numberPickerHelper.getNumberPickerData(minutesBetweenAlarmsViewModel.allAvailableVariants)
        numberPicker.wrapSelectorWheel = false
        numberPicker.minValue = numberPickerData.minValue
        numberPicker.maxValue = numberPickerData.maxValue
        numberPicker.displayedValues = numberPickerData.displayedValues
    }

    private fun buildDialog(dialogView: View): AlertDialog {
        val dialogBuilder = AlertDialog.Builder(requireContext())

        dialogBuilder.setView(dialogView)

        dialogBuilder.setPositiveButton(R.string.dialog_ok_button_name) {
            dialog, which -> minutesBetweenAlarmsViewModel.onOkButtonClickInMinutesBetweenAlarmsDialog()
        }

        dialogBuilder.setNegativeButton(R.string.dialog_cancel_button_name) {
            dialog, which -> minutesBetweenAlarmsViewModel.onCancelButtonClickInMinutesBetweenAlarmsDialog()
        }

        return dialogBuilder.create()
    }


}