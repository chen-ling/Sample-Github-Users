package ccl.exercise.githubusers.service

import ccl.exercise.githubusers.model.User
import kotlinx.coroutines.Deferred

interface GithubService {
    fun getUsersAsync(since: Long?): Deferred<List<User>>
    fun getUserDetailAsync(username: String): Deferred<User>
}