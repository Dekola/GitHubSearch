package com.dekola.githublogin

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.dekola.githublogin.ui.LoginActivity
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginActivityTest {

    private var mIdlingResource: IdlingResource? = null

//    @get:Rule
//    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun registerIdlingResource() {
        val activityScenario: ActivityScenario<LoginActivity> =
            ActivityScenario.launch(LoginActivity::class.java)
        activityScenario.onActivity { activity ->
            mIdlingResource = activity?.getIdlingResource()
            IdlingRegistry.getInstance().register(mIdlingResource)
        }
    }

    @Test
    fun githubSearch() {
        onView(withId(R.id.search_et))
            .perform(ViewActions.typeText("test"), ViewActions.closeSoftKeyboard())

        onView(withId(R.id.login_button)).perform(ViewActions.click())
    }

    @After
    fun unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource)
        }
    }
}