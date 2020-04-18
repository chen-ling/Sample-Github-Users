package ccl.exercise.githubusers.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: Int,
    @SerializedName("avatar_url")
    val avatarUrl: String?,
    @SerializedName("login")
    val name: String,
    @SerializedName("bio")
    val bio: String,
    @SerializedName("site_admin")
    val isAdmin: Boolean,
    @SerializedName("location")
    val location: String,
    @SerializedName("blog")
    val blog: String
)