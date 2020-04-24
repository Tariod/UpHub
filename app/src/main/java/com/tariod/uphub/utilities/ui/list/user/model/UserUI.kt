package com.tariod.uphub.utilities.ui.list.user.model

import com.tariod.uphub.data.database.model.User
import com.tariod.uphub.ui.base.Item

data class UserUI(val user: User? = null) : Item<UserUI> {

    override fun eqId(item: UserUI): Boolean =
        if (item.user != null && user != null)
            with(user) { item.user.id == id }
        else
            user == null && item.user == null

    override fun eqUI(item: UserUI): Boolean =
        if (item.user != null && user != null)
            with(user) {
                eqId(item)
                        && item.user.name == name
                        && item.user.login == login
                        && item.user.bio == bio
                        && item.user.location == location
            }
        else
            user == null && item.user == null
}