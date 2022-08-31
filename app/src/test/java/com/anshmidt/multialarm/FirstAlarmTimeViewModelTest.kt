package com.anshmidt.multialarm


class FirstAlarmTimeViewModelTest {

//    @Mock
//    private lateinit var settingsRepository: ScheduleSettingsRepository
//
//    @Mock
//    private lateinit var alarmScheduler: AlarmScheduler
//
//    private lateinit var firstAlarmTimeViewModel: FirstAlarmTimeViewModel
//
//
//    // Allows to set livedata
//    @get:Rule
//    var instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()
//
//    @Before
//    fun setUp() {
//        MockitoAnnotations.initMocks(this)
//
//        firstAlarmTimeViewModel = FirstAlarmTimeViewModel(settingsRepository, alarmScheduler)
//    }
//
//    @Test
//    fun displayedFormat_ofFirstAlarmTime() {
//        // given
//        val firstAlarmTime = LocalTime.of(1, 9)
//        `when`(settingsRepository.firstAlarmTime).thenReturn(firstAlarmTime)
//        val expectedDisplayableTime = "01:09"
//        firstAlarmTimeViewModel.firstAlarmTimeDisplayable.observeForever { } //needed to be called in order to read livedata value
//
//        // when
//        firstAlarmTimeViewModel.onViewCreated()
//
//        val actualDisplayableTime = firstAlarmTimeViewModel.firstAlarmTimeDisplayable.value
//
//        // then
//        Assert.assertEquals(expectedDisplayableTime, actualDisplayableTime)
//    }
//
//    @Test
//    fun alarmRescheduled_whenFirstAlarmTimeChanged() {
//        firstAlarmTimeViewModel._firstAlarmTime.value = LocalTime.of(1,9)
//        firstAlarmTimeViewModel.onFirstAlarmTimeSelectedOnPicker(1, 9)
//        firstAlarmTimeViewModel.onOkButtonClickInFirstAlarmDialog()
//        verify(alarmScheduler).reschedule(anyOrNull())
//    }


}