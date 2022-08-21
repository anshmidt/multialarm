package com.anshmidt.multialarm.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.anshmidt.multialarm.R
import com.anshmidt.multialarm.databinding.FragmentFirstAlarmTimeBinding
import com.anshmidt.multialarm.view.dialogs.FirstAlarmTimeDialogFragment
import com.anshmidt.multialarm.viewmodel.FirstAlarmTimeViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FirstAlarmTimeFragment : Fragment() {
    private val firstAlarmTimeViewModel: FirstAlarmTimeViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentFirstAlarmTimeBinding>(
                requireActivity().layoutInflater,
                R.layout.fragment_first_alarm_time,
                null,
                false
        )

        initBinding(binding)
        val view = binding.root

        firstAlarmTimeViewModel.openFirstAlarmTimeDialog.observe(viewLifecycleOwner, Observer {
            openFirstAlarmTimeDialog()
        })

        firstAlarmTimeViewModel.onViewCreated()
        return view
    }

    private fun initBinding(binding: FragmentFirstAlarmTimeBinding) {
        binding.lifecycleOwner = this
        binding.viewModel = firstAlarmTimeViewModel
    }

    private fun openFirstAlarmTimeDialog() {
        val dialog = FirstAlarmTimeDialogFragment()
        dialog.show(requireActivity().supportFragmentManager, FirstAlarmTimeDialogFragment.FRAGMENT_TAG)
    }

    override fun onResume() {
        super.onResume()
        firstAlarmTimeViewModel.onViewResumed()
    }

    override fun onPause() {
        super.onPause()
        firstAlarmTimeViewModel.onViewPaused()
    }

    override fun onDestroy() {
        super.onDestroy()
        firstAlarmTimeViewModel.onViewDestroyed()
    }
}