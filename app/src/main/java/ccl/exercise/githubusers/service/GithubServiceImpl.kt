package ccl.exercise.githubusers.service

import ccl.exercise.githubusers.model.User
import kotlinx.coroutines.Deferred

class GithubServiceImpl(private val githubApi: GithubApi) : GithubService {
    override fun getUsersAsync(since: Int): Deferred<List<User>> =
        githubApi.getUsers(since = since)

    override fun getUserDetailAsync(username: String): Deferred<User> =
        githubApi.getUserDetail(username)
}