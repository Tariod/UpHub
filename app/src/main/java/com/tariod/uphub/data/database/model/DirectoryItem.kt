package com.tariod.uphub.data.database.model

import androidx.room.Entity

@Entity(primaryKeys = ["sha", "parent"])
data class DirectoryItem(
    val sha: String,
    val parent: String,
    val name: String,
    val isFolder: Boolean,
    val size: Long
) {

    companion object {

        fun getRoot(sha: String, reposName: String) = DirectoryItem(sha, "", reposName, false, 0)
    }
}