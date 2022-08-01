package com.anshmidt.multialarm.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.anshmidt.multialarm.R
import com.anshmidt.multialarm.databinding.FragmentNumberOfAlarmsBinding
import com.anshmidt.multialarm.view.dialogs.NumberOfAlarmsDialogFragment
import com.anshmidt.multialarm.viewmodel.NumberOfAlarmsViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class NumberOfAlarmsFragment : Fragment() {
    private val numberOfAlarmsViewModel: NumberOfAlarmsViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentNumberOfAlarmsBinding>(
                requireActivity().layoutInflater,
                R.layout.fragment_number_of_alarms,
                null,
                false
        )

        initBinding(binding)
        val view = binding.root

        numberOfAlarmsViewModel.openNumberOfAlarmsDialog.observe(this, Observer {
            openNumberOfAlarmsDialog()
        })

        numberOfAlarmsViewModel.onViewCreated()
        return view
    }

    private fun initBinding(binding: FragmentNumberOfAlarmsBinding) {
        binding.lifecycleOwner = this
        binding.numberOfAlarmsViewModel = numberOfAlarmsViewModel
    }

    private fun openNumberOfAlarmsDialog() {
        val dialog = NumberOfAlarmsDialogFragment()
        dialog.show(requireActivity().supportFragmentManager, NumberOfAlarmsDialogFragment.FRAGMENT_TAG)
    }

}