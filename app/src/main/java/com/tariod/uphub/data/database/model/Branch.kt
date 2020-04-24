package com.tariod.uphub.data.database.model

import androidx.room.Entity

@Entity(primaryKeys = ["name", "reposId"])
data class Branch(val name: String, val reposId: Int, val lastSha: String)