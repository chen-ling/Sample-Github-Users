package ccl.exercise.githubusers.ui.userlist

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import ccl.exercise.githubusers.R
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserListFragmentTest {

    @Test
    fun testNavigationToUserDetail() {
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )
        navController.setGraph(R.navigation.nav_graph)

        launchFragmentInContainer<UserListFragment>().apply {
            onFragment { fragment ->
                Navigation.setViewNavController(fragment.requireView(), navController)
            }
        }
        onView(ViewMatchers.withId(R.id.navToUserDetailText)).perform(ViewActions.click())
        assertThat(navController.currentDestination?.id, equalTo(R.id.userDetailFragment))
    }

}