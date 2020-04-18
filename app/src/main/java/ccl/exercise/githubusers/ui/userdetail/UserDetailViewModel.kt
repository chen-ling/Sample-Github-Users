package ccl.exercise.githubusers.ui.userdetail

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ccl.exercise.githubusers.model.User
import ccl.exercise.githubusers.service.GithubService
import kotlinx.coroutines.*
import org.koin.core.KoinComponent
import org.koin.core.inject

class UserDetailViewModel(val name: String) : ViewModel(), KoinComponent {
    companion object {
        private val TAG = UserDetailViewModel::class.java.simpleName
    }

    private val service: GithubService by inject()

    private val _error: MutableLiveData<Exception> = MutableLiveData()
    val error: LiveData<Exception>
        get() = _error

    val isLoading = MutableLiveData(true)
    val user = MutableLiveData<User>()

    @VisibleForTesting
    var job: Job? = null

    fun fetchUserProfile() {
        val currentUser = user.value
        if (currentUser != null) {
            user.postValue(currentUser)
            return
        }
        job = CoroutineScope(Dispatchers.IO).launch {
            isLoading.postValue(true)
            try {
                user.postValue(service.getUserDetailAsync(name))
            } catch (e: Exception) {
                handlerError(e)
            } finally {
                isLoading.postValue(false)
            }
        }
    }

    private fun handlerError(e: Exception) {
        Log.e(TAG, "error: ${e.message}")
        _error.postValue(e)
    }

    override fun onCleared() {
        job?.cancel()
    }
}