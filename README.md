# MultiAlarm
MultiAlarm is an alarm clock app for Android. It is designed to help users who likes to set multiple alarm clocks for waking up in the morning. It provides simple and effective way to manage them all at once.

![multialarm screenshots](https://user-images.githubusercontent.com/12444628/236220779-4f383ccc-a2a8-4774-a21a-d8743cc0a098.png)
<br/><br/><br/>
Unlike most alarm apps, MultiAlarm doesn’t force user to choose between “snooze” and “dismiss” when he has just woken up and only wants to stop that annoying thing ringing. It's impossible to accidentally turn off all the alarms when trying to dismiss a ringing alarm.   
<img src="https://github.com/anshmidt/multialarm/assets/12444628/68d51298-c18c-4658-b991-55d73338e814" width="280"/>
<br/><br/>

Alarms simply ring on schedule until the user turns them off from the main menu. 

It's super easy to manage many alarms all at once. The user simply sets the time of the first alarm, the interval between alarms and the number of alarms. The switch can turn on and off all the alarms together.  
<img src="https://github.com/anshmidt/multialarm/assets/12444628/0acf2f18-c690-41ff-b68e-81bb72dfdd25" width="280"/>
<br/><br/>

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
<br/><br/>

## Permission info: <br/>
WAKE_LOCK - allows to display “Dismiss” screen if device is locked <br/>
MODIFY_AUDIO_SETTINGS - allows alarm to ring if device switched to “Vibrate” mode <br/>
READ_EXTERNAL_STORAGE - is needed to load external ringtones <br/>
ACCESS_NOTIFICATION_POLICY - alarm can ring in “Not Disturb” mode if user allows that <br/>

## Image resources credits:<br/>
<a href="https://www.freepik.com/free-vector/meadow-with-pond-conifers-hills-night_23007728.htm#query=meadow%20with%20pond%20conifers%20hills%20night&position=0&from_view=search">Image by upklyak</a> on Freepik<br/>
<a href="https://www.vecteezy.com">Image by chai-ky</a> on Vecteezy<br/>
