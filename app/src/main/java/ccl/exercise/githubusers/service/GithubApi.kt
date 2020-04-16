package ccl.exercise.githubusers.service

import ccl.exercise.githubusers.model.User
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApi {
    companion object {
        const val API_ENDPOINT = "https://api.github.com"
        private const val PAGE_SIZE = 50
    }

    @GET("/users")
    fun getUsers(
        @Query("per_page") pageSize: Int = PAGE_SIZE,
        @Query("since") since: Int = 0
    ): Deferred<List<User>>

    @GET("/users/{username}")
    fun getUserDetail(@Path("username") username: String): Deferred<User>
}