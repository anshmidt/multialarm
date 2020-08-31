package com.anshmidt.multialarm.view

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.anshmidt.multialarm.R
import com.anshmidt.multialarm.viewmodel.MainViewModel

class FirstAlarmTimeDialogFragment : DialogFragment() {

    companion object {
        val FRAGMENT_TAG = FirstAlarmTimeDialogFragment::class.java.simpleName
    }

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = requireActivity().layoutInflater.inflate(R.layout.dialog_first_alarm_time, null)
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setView(dialogView)

        dialogBuilder.setPositiveButton(R.string.dialog_ok_button_name) {
            dialog, which -> onOkButtonClick()
        }

        dialogBuilder.setNegativeButton(R.string.dialog_cancel_button_name) {
            dialog, which -> onCancelButtonClick()
        }

        Log.d(FRAGMENT_TAG, "number of alarms: ${viewModel.numberOfAlarms}")

        return dialogBuilder.create()
    }

    fun onOkButtonClick() {

    }

    fun onCancelButtonClick() {

    }
}