package com.anshmidt.multialarm.view.dialogs

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
import com.anshmidt.multialarm.databinding.DialogNumberOfAlarmsBinding
import com.anshmidt.multialarm.view.helpers.NumberPickerHelper
import com.anshmidt.multialarm.viewmodel.NumberOfAlarmsViewModel

class NumberOfAlarmsDialogFragment : DialogFragment() {

    companion object {
        val FRAGMENT_TAG = NumberOfAlarmsDialogFragment::class.java.simpleName
    }

    private val numberOfAlarmsViewModel: NumberOfAlarmsViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val binding = DataBindingUtil.inflate<DialogNumberOfAlarmsBinding>(
                requireActivity().layoutInflater,
                R.layout.dialog_number_of_alarms,
                null,
                false
        )

        initBinding(binding)

        val dialogView = binding.root

        initNumberPicker(dialogView)

        return buildDialog(dialogView)
    }

    private fun initBinding(binding: DialogNumberOfAlarmsBinding) {
        binding.lifecycleOwner = this
        binding.numberOfAlarmsViewModel = numberOfAlarmsViewModel
    }

    private fun initNumberPicker(dialogView: View) {
        val numberPicker = dialogView.findViewById<NumberPicker>(R.id.numberpicker_numberdialog)
        val numberPickerHelper = NumberPickerHelper()
        val numberPickerData = numberPickerHelper.getNumberPickerData(numberOfAlarmsViewModel.allAvailableVariants)
        numberPicker.wrapSelectorWheel = false
        numberPicker.minValue = numberPickerData.minValue
        numberPicker.maxValue = numberPickerData.maxValue
        numberPicker.displayedValues = numberPickerData.displayedValues
    }

    private fun buildDialog(dialogView: View): AlertDialog {
        val dialogBuilder = AlertDialog.Builder(requireContext())

        dialogBuilder.setView(dialogView)

        dialogBuilder.setPositiveButton(R.string.dialog_ok_button_name) {
            dialog, which -> numberOfAlarmsViewModel.onOkButtonClickInNumberOfAlarmsDialog()
        }

        dialogBuilder.setNegativeButton(R.string.dialog_cancel_button_name) {
            dialog, which -> numberOfAlarmsViewModel.onCancelButtonClickInNumberOfAlarmsDialog()
        }

        return dialogBuilder.create()
    }

}