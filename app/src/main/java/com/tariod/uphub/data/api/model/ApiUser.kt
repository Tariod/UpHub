package com.tariod.uphub.data.api.model

import com.google.gson.annotations.SerializedName
import com.tariod.uphub.data.database.model.User

data class ApiUser(
    val id: Int,
    val login: String,
    val name: String?,
    val company: String?,
    val location: String?,
    val email: String?,
    val bio: String?,
    val followers: Int,
    val following: Int,
    @SerializedName("public_repos") val publicRepos: Int,
    @SerializedName("avatar_url") val avatarUrl: String
) {

    fun asGeneralModel() = User(
        id,
        login,
        name ?: login,
        company ?: "",
        location ?: "",
        email ?: "",
        bio ?: "",
        followers,
        following,
        publicRepos,
        avatarUrl
    )
}