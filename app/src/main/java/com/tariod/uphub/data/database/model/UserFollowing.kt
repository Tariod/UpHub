package com.tariod.uphub.data.database.model

import androidx.room.Entity

@Entity(primaryKeys = ["userId", "followerId"])
data class UserFollowing(val userId: Int, val followerId: Int)