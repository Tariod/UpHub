package com.tariod.uphub.data.api.model

import com.tariod.uphub.data.database.model.Branch

data class ApiBranch(val name: String, val commit: ApiCommit) {

    fun asGeneralModel(reposId: Int) = Branch(name, reposId, commit.sha)
}