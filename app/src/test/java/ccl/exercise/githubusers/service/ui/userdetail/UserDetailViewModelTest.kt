package ccl.exercise.githubusers.service.ui.userdetail

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import ccl.exercise.githubusers.model.User
import ccl.exercise.githubusers.service.GithubService
import ccl.exercise.githubusers.ui.userdetail.UserDetailViewModel
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.get
import org.koin.test.inject
import retrofit2.HttpException


class UserDetailViewModelTest : KoinTest {
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
        factory { mockkClass(HttpException::class) }
        viewModel { UserDetailViewModel("name") }
    }

    private val service: GithubService by inject()
    private val viewModel: UserDetailViewModel by inject()

    @Test
    fun `can get profile`() {
        val mockUser = mockkClass(User::class)
        coEvery { service.getUserDetailAsync(any()) } returns mockUser
        runBlocking {
            viewModel.fetchUserProfile()
            viewModel.job?.join()
        }
        coVerify(exactly = 1) { service.getUserDetailAsync(any()) }
        assertEquals(mockUser, viewModel.user.value)
        assertFalse(viewModel.isLoading.value ?: true)
    }

    @Test
    fun `get error on fetching user profile`() {
        mockkStatic(Log::class).apply { every { Log.e(any(), any()) } returns 0 }
        val errStr = "this is a fake error"
        val httpException: HttpException = get()
        httpException.apply { every { message } answers { errStr } }
        coEvery { service.getUserDetailAsync(any()) } throws httpException

        runBlocking {
            viewModel.fetchUserProfile()
            viewModel.job?.join()
            assertFalse(viewModel.isLoading.value ?: true)
            assertTrue(viewModel.error.value != null)
        }
    }
}