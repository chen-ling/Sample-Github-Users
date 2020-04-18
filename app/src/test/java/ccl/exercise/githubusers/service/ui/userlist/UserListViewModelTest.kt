package ccl.exercise.githubusers.service.ui.userlist

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import ccl.exercise.githubusers.model.User
import ccl.exercise.githubusers.service.GithubApi
import ccl.exercise.githubusers.service.GithubService
import ccl.exercise.githubusers.ui.userlist.UserListViewModel
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject


class UserListViewModelTest : KoinTest {
    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(mockAppModules)
    }

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val mockAppModules = module {
        single {
            mockkClass(GithubService::class)
        }
        viewModel { UserListViewModel() }
    }

    private val service: GithubService by inject()
    private val viewModel: UserListViewModel by inject()

    @Test
    fun `can get users`() {
        val mockUser = mockkClass(User::class)
        coEvery { service.getUsersAsync(eq(GithubApi.PAGE_SIZE)) } returns listOf(mockUser)
        runBlocking {
            viewModel.fetchUsers()
            viewModel.job?.join()
        }
        coVerify(exactly = 1) { service.getUsersAsync(eq(GithubApi.PAGE_SIZE)) }
        assertEquals(mockUser, viewModel.usersLiveData.value?.get(0))
        assertFalse(viewModel.isLoading.value ?: true)
    }

    @Test
    fun `can get users twice`() {
        val mockUser = mockkClass(User::class).apply { every { id } answers { 1 } }
        val mockUser2 = mockkClass(User::class).apply { every { id } answers { 2 } }
        coEvery { service.getUsersAsync(eq(GithubApi.PAGE_SIZE)) } returns listOf(mockUser)
        coEvery { service.getUsersAsync(eq(GithubApi.PAGE_SIZE), eq(1)) } returns listOf(mockUser2)

        runBlocking {
            viewModel.fetchUsers()
            viewModel.job?.join()
            coVerify(exactly = 1) { service.getUsersAsync(eq(GithubApi.PAGE_SIZE), any()) }
            assertEquals(mockUser, viewModel.usersLiveData.value?.get(0))

            viewModel.fetchUsers()
            viewModel.job?.join()
            coVerify(exactly = 1) { service.getUsersAsync(eq(GithubApi.PAGE_SIZE), eq(1)) }
            assertEquals(mockUser2, viewModel.usersLiveData.value?.get(1))
            assertFalse(viewModel.isLoading.value ?: true)
        }
    }

    @Test
    fun `get error when reaches limit user count`() {
        mockkStatic(Log::class).apply { every { Log.e(any(), any()) } returns 0 }
        val mockUser = mockkClass(User::class).apply { every { id } answers { 1 } }
        val mockUser2 = mockkClass(User::class).apply { every { id } answers { 2 } }
        val mockResult = mutableListOf<User>()
        for (i in 0 until 100) {
            mockResult.add(mockUser)
        }
        coEvery { service.getUsersAsync(eq(GithubApi.PAGE_SIZE)) } returns mockResult.toList()
        coEvery { service.getUsersAsync(eq(GithubApi.PAGE_SIZE), eq(1)) } returns listOf(mockUser2)

        runBlocking {
            viewModel.fetchUsers()
            viewModel.job?.join()
            coVerify(exactly = 1) { service.getUsersAsync(eq(GithubApi.PAGE_SIZE), any()) }

            viewModel.fetchUsers()
            viewModel.job?.join()
            coVerify(exactly = 0) { service.getUsersAsync(eq(GithubApi.PAGE_SIZE), eq(1)) }
            assertFalse(viewModel.isLoading.value ?: true)
        }
    }
}