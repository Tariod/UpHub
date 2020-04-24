package com.tariod.uphub.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tariod.uphub.utilities.mergeMembers

@Entity
data class User(
    @PrimaryKey val id: Int,
    val login: String,
    val name: String,
    val company: String,
    val location: String,
    val email: String,
    val bio: String,
    val followers: Int,
    val following: Int,
    val publicRepos: Int,
    val avatarUrl: String
) {

    fun merge(user: User) = User(
        id,
        login,
        name,
        mergeMembers(company, user.company),
        mergeMembers(location, user.location),
        mergeMembers(email, user.email),
        mergeMembers(bio, user.bio),
        mergeMembers(followers, user.followers),
        mergeMembers(following, user.following),
        mergeMembers(publicRepos, user.publicRepos),
        mergeMembers(avatarUrl, user.avatarUrl)
    )
}