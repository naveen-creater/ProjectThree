package com.example.projectthree;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.projectthree.Activity.TestUiActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.projectthree", appContext.getPackageName());

    }

    @Rule
    public ActivityTestRule<TestUiActivity> activityRule = new ActivityTestRule<>(TestUiActivity.class,false);

    @Test
    public void ensureTextExist(){
        onView(withId(R.id.name)).perform(typeText("Naveen Kumar"));
    }

    @Test
    public void reTypeText(){
        onView(withId(R.id.name)).perform(clearText());
        onView(withId(R.id.name)).perform(typeText("sathish"));
    }




}