package ccl.exercise.githubusers.service

import ccl.exercise.githubusers.model.User
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApi {
    companion object {
        const val API_ENDPOINT = "https://api.github.com"
        const val PAGE_SIZE = 20
    }

    @GET("/users")
    suspend fun getUsers(
        @Query("per_page") pageSize: Int = PAGE_SIZE,
        @Query("since") since: Int = 0
    ): List<User>

    @GET("/users/{username}")
    suspend fun getUserDetail(@Path("username") username: String): User
}