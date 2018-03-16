package com.anshmidt.multialarm;

import android.content.Context;
import android.preference.Preference;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import android.test.ActivityInstrumentationTestCase2;

import com.anshmidt.multialarm.activities.MainActivity;
import com.anshmidt.multialarm.dialogs.PrefFragment;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.PreferenceMatchers.withKey;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static android.support.test.espresso.Espresso.onData;
import static org.junit.Assert.assertEquals;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;


/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

//    @Test
//    public void useAppContext() throws Exception {
//        // Context of the app under test.
//        Context appContext = InstrumentationRegistry.getTargetContext();
//        assertEquals("com.anshmidt.multialarm", appContext.getPackageName());
//    }

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class);


    @Test
    public void openPrefActivity() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        // Click the item.
        onView(withText("Settings"))
                .perform(click());

        pressBack();

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        // Click the item.
        onView(withText("Settings"))
                .perform(click());

        pressBack();

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        // Click the item.
        onView(withText("Settings"))
                .perform(click());

        pressBack();


//        // Click menu
//        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
//
//        // Choose item "Settings"
//        onView(withId(R.id.action_settings)).perform(click());

        // click settings item
        String prefKey = getTargetContext().getString(R.string.key_ringtone_filename);
        onData(allOf(
                is(instanceOf(Preference.class)),
                withKey(prefKey)))
                .onChildView(withText("Settings"))
                .perform(click());
        //onData(allOf(is(instanceOf(Preference.class)), withKey(prefKey))).onChildView(withClassName(is(Switch.class.getName()))).perform(click());
    }
}
