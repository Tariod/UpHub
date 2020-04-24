package com.tariod.uphub.utilities.ui.list.dir.model

import com.tariod.uphub.data.database.model.DirectoryItem
import com.tariod.uphub.ui.base.Item

data class DirUI(val dir: DirectoryItem? = null) : Item<DirUI> {

    override fun eqId(item: DirUI): Boolean =
        if (dir != null && item.dir != null)
            with(dir) { item.dir.sha == sha }
        else
            dir == null && item.dir == null

    override fun eqUI(item: DirUI): Boolean =
        if (dir != null && item.dir != null)
            with(dir) {
                eqId(item)
                        && item.dir.name == name
                        && item.dir.size == size
                        && item.dir.isFolder == isFolder
            }
        else
            eqId(item)
}