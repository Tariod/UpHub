package com.tariod.uphub.data.api.model

import com.google.gson.annotations.SerializedName
import com.tariod.uphub.data.database.model.Repository
import com.tariod.uphub.utilities.parseGithubTime

data class ApiRepository(
    val id: Int,
    val name: String,
    val owner: ApiUser,
    val private: Boolean,
    val description: String,
    val fork: Boolean,
    @SerializedName("created_at") val createdAt: String?
) {

    fun asGeneralModel() = Repository(
        id,
        name,
        owner.id,
        private,
        description,
        fork,
        parseGithubTime(createdAt ?: "").time
    ) to owner.asGeneralModel()
}