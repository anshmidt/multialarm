package com.anshmidt.multialarm.view.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.anshmidt.multialarm.R
import com.anshmidt.multialarm.databinding.DialogLogBinding
import com.anshmidt.multialarm.viewmodel.LogViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LogDialogFragment : DialogFragment() {

    companion object {
        val FRAGMENT_TAG = LogDialogFragment::class.java.simpleName
    }

    private val viewModel: LogViewModel by viewModel()
    private var binding: DialogLogBinding? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DataBindingUtil.inflate<DialogLogBinding>(
            requireActivity().layoutInflater,
            R.layout.dialog_log,
            null,
            false
        )

        initBinding(binding)
        val dialogView = binding.root
        this.binding = binding
        return buildDialog(dialogView)
    }

    private fun initBinding(binding: DialogLogBinding) {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    override fun onResume() {
        super.onResume()
        viewModel.onViewCreated()
        binding?.let {
            it.scrollviewLogdialog.postDelayed({ // scrollview is automatically scrolled down
                it.scrollviewLogdialog.fullScroll(View.FOCUS_DOWN)
            }, 100)
        }
    }

    private fun buildDialog(dialogView: View): AlertDialog {
        val dialogBuilder = AlertDialog.Builder(requireContext(), R.style.DialogStyle)

        dialogBuilder.setView(dialogView)

        dialogBuilder.setNeutralButton(R.string.dialog_close_button_name) {
            dialog, which -> {}
        }

        return dialogBuilder.create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}