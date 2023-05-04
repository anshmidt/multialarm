package com.anshmidt.multialarm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.anshmidt.multialarm.data.SingleLiveEvent
import com.anshmidt.multialarm.logging.Log

class DismissAlarmViewModel : ViewModel() {

    private val _finishView = SingleLiveEvent<Any>()
    val finishView: LiveData<Any>
        get() = _finishView

    private val _stopMusicService = SingleLiveEvent<Any>()
    val stopMusicService: LiveData<Any>
        get() = _stopMusicService

    private var reasonWhyFinishView: ReasonWhyFinishView? = null

    fun onViewCreated() {

    }

    /**
     * When MusicService is destroyed, it doesn't finish the view.
     */
    fun onDismissButtonClicked() {
        Log.d(TAG, "Dismiss button clicked")
        reasonWhyFinishView = ReasonWhyFinishView.DISMISS_BUTTON_CLICKED
        _stopMusicService.call()
        _finishView.call()
    }

    /**
     * Method onPause() was called during the process of DismissAlarmActivity creation.
     * That's why we use other methods to check if view is in active state.
     */
    fun onViewGainedFocus() {
        Log.d(TAG, "onViewGainedFocus")
        reasonWhyFinishView = ReasonWhyFinishView.VIEW_PAUSED_BY_SYSTEM
    }

    /**
     * If something appears on top of DismissAlarm screen (for example incoming call screen or another app),
     * then we stop the music and close the DismissAlarm screen.
     * Remember that this is also called after user clicks Dismiss, or count down finishes.
     */
    fun onViewLostFocus() {
        Log.d(TAG, "onViewLostFocus. reasonWhyFinishView: $reasonWhyFinishView")
        if (reasonWhyFinishView == ReasonWhyFinishView.VIEW_PAUSED_BY_SYSTEM) {
            _stopMusicService.call()
            _finishView.call()
        }
    }

    /**
     * On count down finish, MusicService informs the view by sending intent to DismissAlarmActivity
     * (and after that, the service stops itself). When the intent is received in activity,
     * this method is triggered.
     */
    fun onCountDownFinished() {
        reasonWhyFinishView = ReasonWhyFinishView.COUNT_DOWN_FINISHED
        _finishView.call()
    }

    enum class ReasonWhyFinishView {
        COUNT_DOWN_FINISHED,
        DISMISS_BUTTON_CLICKED,
        VIEW_PAUSED_BY_SYSTEM
    }

    companion object {
        val TAG = DismissAlarmViewModel::class.java.simpleName
    }

}