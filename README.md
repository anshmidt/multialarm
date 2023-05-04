# MultiAlarm
MultiAlarm is an alarm clock app for Android. It is designed to help users who likes to set multiple alarm clocks for waking up in the morning. It provides simple and effective way to manage them all at once.

![multialarm screenshots](https://user-images.githubusercontent.com/12444628/236220779-4f383ccc-a2a8-4774-a21a-d8743cc0a098.png)
<br/><br/>
Unlike most alarm apps, MultiAlarm doesn’t force you to choose between “snooze” and “dismiss” when you've just woken up and only want to stop that annoying thing ringing. It's not possible to accidentally turn off all the alarms when trying to dismiss a ringing alarm.<br/><br/>
Alarms simply ring according to schedule until you turn them off from the main menu.

<img src="https://user-images.githubusercontent.com/12444628/236221916-f7caa908-7436-4c1a-96f9-0a59f7e224f6.gif" width="280"/>

## Used technologies:
- Kotlin
- MVVM
- LiveData
- Data Binding
- Kotlin Flow
- Preferences DataStore
- Koin
- Mockito
- Robolectric

## Permission info: <br/>
WAKE_LOCK - allows to display “Dismiss” screen if device is locked <br/>
MODIFY_AUDIO_SETTINGS - allows alarm to ring if device switched to “Vibrate” mode <br/>
READ_EXTERNAL_STORAGE - is needed to load external ringtones <br/>
ACCESS_NOTIFICATION_POLICY - alarm can ring in “Not Disturb” mode if user allows that <br/>

## Image resources credits:<br/>
<a href="https://www.freepik.com/free-vector/meadow-with-pond-conifers-hills-night_23007728.htm#query=meadow%20with%20pond%20conifers%20hills%20night&position=0&from_view=search">Image by upklyak</a> on Freepik<br/>
<a href="https://www.vecteezy.com">Image by chai-ky</a> on Vecteezy<br/>
