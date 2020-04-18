package ccl.exercise.githubusers.ui.userlist

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import ccl.exercise.githubusers.R
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserListFragmentTest {
    companion object {
        private const val THREE_SECONDS = 3000L
    }

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
//        FIXME a workaround for waiting recyclerView updated
        Thread.sleep(THREE_SECONDS)
        onView(withId(R.id.usersRecyclerView)).perform(
            actionOnItemAtPosition<UserViewHolder>(
                0,
                ViewActions.click()
            )
        )
        assertThat(navController.currentDestination?.id, equalTo(R.id.userDetailFragment))
    }

}