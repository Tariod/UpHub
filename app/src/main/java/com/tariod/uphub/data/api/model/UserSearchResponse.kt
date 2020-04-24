package com.tariod.uphub.data.api.model

import com.google.gson.annotations.SerializedName

data class UserSearchResponse(
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("incomplete_results") val incompleteResults: Boolean,
    val items: List<ApiUser>
)