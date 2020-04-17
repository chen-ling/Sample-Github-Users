package ccl.exercise.githubusers.service

import ccl.exercise.githubusers.KoinModules
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.koin.core.KoinComponent
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject

@Ignore
/** Check if apis are available */
class GithubServiceTest : KoinTest, KoinComponent {
    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(KoinModules.appModules)
    }
    private val githubService: GithubService by inject()

    @Test
    fun `get 50 users`() = runBlocking {
        val users = githubService.getUsersAsync().await()
        Assert.assertEquals(50, users.size)
    }

    @Test
    fun `get 100 users`() = runBlocking {
        val firstFetchedUsers = githubService.getUsersAsync().await()
        val secondFetchedUsers =
            githubService.getUsersAsync(firstFetchedUsers.size).await()
        Assert.assertEquals(100, firstFetchedUsers.size + secondFetchedUsers.size)
    }

    @Test
    fun `get user detail`() = runBlocking {
        githubService.getUserDetailAsync("chen-ling").await().let(::print)
    }
}