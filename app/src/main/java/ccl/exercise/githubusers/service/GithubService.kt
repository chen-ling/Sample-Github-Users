package ccl.exercise.githubusers.service

import ccl.exercise.githubusers.model.User
import ccl.exercise.githubusers.service.GithubApi.Companion.PAGE_SIZE

interface GithubService {
    suspend fun getUsersAsync(pageSize: Int = PAGE_SIZE, since: Int = 0): List<User>
    suspend fun getUserDetailAsync(username: String): User
}