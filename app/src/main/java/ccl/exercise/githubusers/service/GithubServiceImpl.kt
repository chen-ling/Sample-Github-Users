package ccl.exercise.githubusers.service

import ccl.exercise.githubusers.model.User

class GithubServiceImpl(private val githubApi: GithubApi) : GithubService {
    override suspend fun getUsersAsync(pageSize: Int, since: Int): List<User> =
        githubApi.getUsers(pageSize, since)

    override suspend fun getUserDetailAsync(username: String): User =
        githubApi.getUserDetail(username)
}