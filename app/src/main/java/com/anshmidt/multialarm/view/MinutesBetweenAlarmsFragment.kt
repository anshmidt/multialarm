package com.anshmidt.multialarm.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.anshmidt.multialarm.R
import com.anshmidt.multialarm.databinding.FragmentMinutesBetweenAlarmsBinding
import com.anshmidt.multialarm.viewmodel.MainViewModel
import com.anshmidt.multialarm.viewmodel.MinutesBetweenAlarmsViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MinutesBetweenAlarmsFragment : Fragment() {

    private val minutesBetweenAlarmsViewModel: MinutesBetweenAlarmsViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentMinutesBetweenAlarmsBinding>(
                requireActivity().layoutInflater,
                R.layout.fragment_minutes_between_alarms,
                null,
                false
        )

        initBinding(binding)
        val view = binding.root
        return view
    }

    private fun initBinding(binding: FragmentMinutesBetweenAlarmsBinding) {
        binding.lifecycleOwner = this
        binding.minutesBetweenAlarmsViewModel = minutesBetweenAlarmsViewModel
    }


}