package com.tariod.uphub.data.api.model

import com.tariod.uphub.data.database.model.DirectoryItem

data class ApiDirectoryItem(val path: String, val sha: String, val type: String, val size: Long) {

    fun asGeneralModel(parent: String) = DirectoryItem(sha, parent, path, type == "tree", size)
}