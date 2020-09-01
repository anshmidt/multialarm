package com.anshmidt.multialarm.view

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.anshmidt.multialarm.R
import com.anshmidt.multialarm.databinding.DialogFirstAlarmTimeBinding
import com.anshmidt.multialarm.viewmodel.MainViewModel

class FirstAlarmTimeDialogFragment : DialogFragment() {

    companion object {
        val FRAGMENT_TAG = FirstAlarmTimeDialogFragment::class.java.simpleName
    }

    private val viewModel: MainViewModel by activityViewModels()

//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
////        val binding = DialogFirstAlarmTimeBinding.inflate(inflater, container, false)
////        binding.mainViewModel = viewModel
////        return binding.root
//        val binding = DataBindingUtil.inflate<DialogFirstAlarmTimeBinding>(inflater, R.layout.dialog_first_alarm_time, container, false)
//        binding.mainViewModel = viewModel
//        val dialogView = binding.root
//
//        return dialogView
//    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val binding = DataBindingUtil.inflate<DialogFirstAlarmTimeBinding>(requireActivity().layoutInflater, R.layout.dialog_first_alarm_time, null, false)
        binding.mainViewModel = viewModel
        val dialogView = binding.root

//        val dialogView = requireActivity().layoutInflater.inflate(R.layout.dialog_first_alarm_time, null)
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