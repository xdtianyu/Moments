package org.xdty.moments;

import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xdty.moments.activity.MainActivity_;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by ty on 15-11-4.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule public final ActivityTestRule<MainActivity_> main = new ActivityTestRule<>(MainActivity_.class);

    @Test
    public void shouldBeAbleLaunchMainScreen() {
        onView(withId(R.id.swipe_refresh_layout)).check(ViewAssertions.matches(isDisplayed()));
    }

    @Test
    public void testUserNameExists() {
        try {
            Thread.sleep(10*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.username)).check(ViewAssertions.matches(withText("tianyu")));
    }
}

