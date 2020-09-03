package com.anshmidt.multialarm.view

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.anshmidt.multialarm.R
import com.anshmidt.multialarm.databinding.ActivityMainBinding
import com.anshmidt.multialarm.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.concurrent.fixedRateTimer

class MainActivity : AppCompatActivity(), MainView {

    private val mainViewModel: MainViewModel by viewModel()


    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()

        mainViewModel.openFirstAlarmTimeDialog.observe(this, Observer {
            openFirstAlarmTimeDialog()
        })

        mainViewModel.onActivityCreated()
    }

    private fun initBinding() {
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
    }

    private fun openFirstAlarmTimeDialog() {
        val dialog = FirstAlarmTimeDialogFragment()
        dialog.show(supportFragmentManager, FirstAlarmTimeDialogFragment.FRAGMENT_TAG)
    }

}