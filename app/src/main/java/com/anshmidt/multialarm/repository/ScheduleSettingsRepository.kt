package com.anshmidt.multialarm.repository

import android.util.Log
import com.anshmidt.multialarm.data.AlarmSettings
import com.anshmidt.multialarm.data.AlarmsConverter
import com.anshmidt.multialarm.datasources.DataStoreStorage
import com.anshmidt.multialarm.datasources.SharedPreferencesStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip
import org.threeten.bp.LocalTime

class ScheduleSettingsRepository(
        private val sharedPreferencesStorage: SharedPreferencesStorage,
        private val dataStoreStorage: DataStoreStorage
) : IScheduleSettingsRepository {

//    override var alarmTurnedOn: Boolean
//        get() = sharedPreferencesStorage.alarmTurnedOn
//        set(alarmState) {
//            alarmTurnedOnObservable.onNext(alarmState)
//            sharedPreferencesStorage.alarmTurnedOn = alarmState
//        }

    override suspend fun saveAlarmSwitchState(switchState: Boolean) {
        dataStoreStorage.saveAlarmSwitchState(switchState)
    }

    override fun getAlarmSwitchState(): Flow<Boolean> = dataStoreStorage.getAlarmSwitchState()

    override suspend fun saveMinutesBetweenAlarms(minutesBetweenAlarms: Int) {
        dataStoreStorage.saveMinutesBetweenAlarms(minutesBetweenAlarms)
    }

    override fun getMinutesBetweenAlarms(): Flow<Int> = dataStoreStorage.getMinutesBetweenAlarms()

    override suspend fun saveNumberOfAlarms(numberOfAlarms: Int) {
        dataStoreStorage.saveNumberOfAlarms(numberOfAlarms)
    }

    override fun getNumberOfAlarms(): Flow<Int> = dataStoreStorage.getNumberOfAlarms()

    override suspend fun saveFirstAlarmTime(firstAlarmTime: LocalTime) {
        dataStoreStorage.saveFirstAlarmHours(firstAlarmTime.hour)
        dataStoreStorage.saveFirstAlarmMinutes(firstAlarmTime.minute)
    }

    override fun getFirstAlarmTime(): Flow<LocalTime> = dataStoreStorage.getFirstAlarmHours()
            .zip(dataStoreStorage.getFirstAlarmMinutes()) { hours, minutes ->
                LocalTime.of(hours, minutes)
            }

    override fun getAlarmsList(): Flow<List<LocalTime>> = dataStoreStorage.getAlarmSettings().map {
        Log.d(TAG, "getAlarmList")
        AlarmsConverter.getAlarmsList(
                firstAlarmTime = it.firstAlarmTime,
                minutesBetweenAlarms = it.minutesBetweenAlarms,
                numberOfAlarms = it.numberOfAlarms
        )
    }

//    override fun testFun(): Flow<AlarmSettings> {
//        Log.d(TAG, "testFun")
//        return dataStoreStorage.getTestFun()
//    }

    //    override fun getAlarmsList(): Flow<List<LocalTime>> = dataStoreStorage.getNumberOfAlarms()
//            .combine(dataStoreStorage.getMinutesBetweenAlarms()) { numberOfAlarms, minutesBetweenAlarms ->
//                Log.d("SchedulteSettingRepo.getAlarmsList", "$")
//                return@combine AlarmsConverter.getAlarmsList(
//                        firstAlarmTime = LocalTime.of(5,0 ),
//                        minutesBetweenAlarms = minutesBetweenAlarms,
//                        numberOfAlarms = numberOfAlarms
//                )
//            }

    override fun getAlarmSettings(): Flow<AlarmSettings> = dataStoreStorage.getAlarmSettings()

//    override var firstAlarmTime: LocalTime
//        get() {
//            val hours = sharedPreferencesStorage.firstAlarmHours
//            val minutes = sharedPreferencesStorage.firstAlarmMinutes
//            return LocalTime.of(hours, minutes)
//        }
//        set(value) {
//            sharedPreferencesStorage.firstAlarmHours = value.hour
//            sharedPreferencesStorage.firstAlarmMinutes = value.minute
//        }

//    override var minutesBetweenAlarms: Int
//        get() = sharedPreferencesStorage.minutesBetweenAlarms
//        set(value) {
//            sharedPreferencesStorage.minutesBetweenAlarms = value
//        }
//
//    override var numberOfAlarms: Int
//        get() = sharedPreferencesStorage.numberOfAlarms
//        set(value) {
//            sharedPreferencesStorage.numberOfAlarms = value
//        }

//    override val alarmsListObservable = sharedPreferencesStorage.alarmSettingsChangedObservable
//            .map { getAlarmsList() }

//    override val alarmTurnedOnObservable = BehaviorSubject.createDefault(alarmTurnedOn)
//
//    override fun subscribeOnChangeListener() {
//        sharedPreferencesStorage.subscribeOnChangeListener()
//    }
//
//    override fun unsubscribeOnChangeListener() {
//        sharedPreferencesStorage.unsubscribeOnChangeListener()
//    }

//    override fun clearAll() {
//        sharedPreferencesStorage.clearAll()
//    }
//
//    override fun getSettings(): AlarmSettings {
//        return AlarmSettings(
//                switchState = alarmTurnedOn,
//                firstAlarmTime = firstAlarmTime,
//                minutesBetweenAlarms = minutesBetweenAlarms,
//                numberOfAlarms = numberOfAlarms
//        )
//    }

//    private fun getAlarmsList(): List<LocalTime> {
//        return AlarmsConverter.getAlarmsList(
//                firstAlarmTime = firstAlarmTime,
//                numberOfAlarms = numberOfAlarms,
//                minutesBetweenAlarms = minutesBetweenAlarms
//        )
//    }


    companion object {
        val TAG = ScheduleSettingsRepository::class.java.simpleName
    }

}