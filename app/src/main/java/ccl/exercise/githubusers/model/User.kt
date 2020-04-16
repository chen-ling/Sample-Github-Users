package ccl.exercise.githubusers.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: Int,
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("bio")
    val bio: String,
    @SerializedName("login")
    val login: String,
    @SerializedName("site_admin")
    val isAdmin: Boolean,
    @SerializedName("location")
    val location: String,
    @SerializedName("blog")
    val blog: String
)