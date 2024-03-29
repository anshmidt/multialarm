package com.anshmidt.multialarm.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.anshmidt.multialarm.databinding.FragmentAlarmsListBinding
import com.anshmidt.multialarm.view.AlarmsListAdapter
import com.anshmidt.multialarm.viewmodel.AlarmsListViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class AlarmsListFragment : Fragment() {

    private lateinit var binding: FragmentAlarmsListBinding
    private val viewModel: AlarmsListViewModel by sharedViewModel()
    private lateinit var adapter: AlarmsListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAlarmsListBinding.inflate(inflater, container, false)
        binding.recyclerviewMainAlarmslist.layoutManager = LinearLayoutManager(requireContext())

        adapter = AlarmsListAdapter()
        binding.recyclerviewMainAlarmslist.adapter = adapter

        viewModel.alarms.observe(viewLifecycleOwner, Observer { alarms ->
            adapter.items = alarms
        })

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.onViewStarted()
    }

    override fun onStop() {
        super.onStop()
        viewModel.onViewStopped()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onViewDestroyed()
    }

}