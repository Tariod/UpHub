package com.tariod.uphub.data.api.model

data class ApiDirectory(val sha: String, val tree: List<ApiDirectoryItem>) {

    fun asGeneralModel() = tree.map { it.asGeneralModel(sha) }
}