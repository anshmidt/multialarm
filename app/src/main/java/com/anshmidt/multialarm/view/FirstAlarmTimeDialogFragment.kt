package com.anshmidt.multialarm.view

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.TimePicker
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.anshmidt.multialarm.R
import com.anshmidt.multialarm.databinding.DialogFirstAlarmTimeBinding
import com.anshmidt.multialarm.viewmodel.MainViewModel
import org.threeten.bp.LocalTime

class FirstAlarmTimeDialogFragment : DialogFragment() {

    companion object {
        val FRAGMENT_TAG = FirstAlarmTimeDialogFragment::class.java.simpleName
    }

    private val mainViewModel: MainViewModel by activityViewModels()


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val binding = DataBindingUtil.inflate<DialogFirstAlarmTimeBinding>(
                requireActivity().layoutInflater,
                R.layout.dialog_first_alarm_time,
                null,
                false
        )
        binding.mainViewModel = mainViewModel
        val dialogView = binding.root

        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setView(dialogView)

        dialogBuilder.setPositiveButton(R.string.dialog_ok_button_name) {
            dialog, which -> onOkButtonClick()
        }

        dialogBuilder.setNegativeButton(R.string.dialog_cancel_button_name) {
            dialog, which -> onCancelButtonClick()
        }

        val timePicker: TimePicker = dialogView.findViewById(R.id.timepicker_firstalarmdialog)
        timePicker.setIs24HourView(true)

        Log.d(FRAGMENT_TAG, "number of alarms: ${mainViewModel.numberOfAlarms}")

        return dialogBuilder.create()
    }



    fun onOkButtonClick() {
        mainViewModel.onOkButtonClickInFirstAlarmDialog()
    }

    fun onCancelButtonClick() {
        mainViewModel.onCancelButtonClickInFirstAlarmDialog()
    }
}