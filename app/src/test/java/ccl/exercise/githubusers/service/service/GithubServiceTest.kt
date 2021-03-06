package ccl.exercise.githubusers.service.service

import ccl.exercise.githubusers.KoinModules
import ccl.exercise.githubusers.model.User
import ccl.exercise.githubusers.service.GithubService
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject

@Ignore
/** Check if apis are available */
class GithubServiceTest : KoinTest{
    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(KoinModules.appModules)
    }
    private val githubService: GithubService by inject()

    @Test
    fun `get 50 users`() = runBlocking {
        val users = githubService.getUsersAsync(50)
        Assert.assertEquals(50, users.size)
    }

    @Test
    fun `get 100 users`() = runBlocking {
        val userSet = mutableSetOf<User>()
        userSet.addAll(githubService.getUsersAsync(50))
        val maxId = userSet.maxBy(User::id)!!.id
        userSet.addAll(githubService.getUsersAsync(50, since = maxId))
        Assert.assertEquals(100, userSet.size)
    }

    @Test
    fun `get user detail`() = runBlocking {
        githubService.getUserDetailAsync("chen-ling").let(::print)
    }
}