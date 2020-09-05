package com.anshmidt.multialarm.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import org.threeten.bp.LocalTime

object LiveDataUtil {

    fun <T> combineLiveDataFromDifferentSources(firstSourceLiveData: LiveData<T>,
                                                    secondSourceLiveData: LiveData<T>): LiveData<T> {
        val combinedLiveData = MediatorLiveData<T>()
        combinedLiveData.addSource(firstSourceLiveData, {
            combinedLiveData.value = it
        })
        combinedLiveData.addSource(secondSourceLiveData, {
            combinedLiveData.value = it
        })
        return combinedLiveData
    }

}
