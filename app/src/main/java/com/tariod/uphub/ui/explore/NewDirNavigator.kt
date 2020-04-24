package com.tariod.uphub.ui.explore

import com.tariod.uphub.data.database.model.DirectoryItem


interface NewDirNavigator {

    fun navigateToNewDir(fromSha: String, toDir: DirectoryItem)
}