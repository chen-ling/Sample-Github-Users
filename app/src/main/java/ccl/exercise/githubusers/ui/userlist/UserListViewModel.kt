package ccl.exercise.githubusers.ui.userlist

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ccl.exercise.githubusers.model.User
import ccl.exercise.githubusers.service.GithubService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.lang.UnsupportedOperationException

class UserListViewModel : ViewModel(), KoinComponent {
    companion object {
        private val TAG = UserListViewModel::class.java.simpleName
        const val LOAD_MORE_THRESHOLD = 15
        const val MAX_FETCHED_COUNT = 100
    }

    private val service: GithubService by inject()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val userList = mutableListOf<User>()
    private val _usersLiveData = MutableLiveData(mutableListOf<User>())
    val usersLiveData: LiveData<MutableList<User>>
        get() = _usersLiveData

    private val _error: MutableLiveData<Exception> = MutableLiveData()
    val error: LiveData<Exception>
        get() = _error

    @VisibleForTesting
    var job: Job? = null

    fun fetchUsers() {
        job = CoroutineScope(IO).launch {
            _isLoading.postValue(true)
            try {
                if (reachFetchLimit()) {
                    throw UnsupportedOperationException()
                }
                val maxId = _usersLiveData.value?.maxBy(User::id)?.id ?: 0
                userList.addAll(service.getUsersAsync(since = maxId).toMutableList())
                _usersLiveData.postValue(userList)
            } catch (e: Exception) {
                handlerError(e)
                /** Reached api request limit, set 5 seconds delay to avoid repeated quick fail*/
                delay(5000L)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    private fun reachFetchLimit(): Boolean = MAX_FETCHED_COUNT <= userList.size

    private fun handlerError(e: Exception) {
        Log.e(TAG, "error: ${e.message}")
        _error.postValue(e)
    }

    override fun onCleared() {
        job?.cancel()
    }
}