package com.tariod.uphub.ui.main.profile.follower

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.tariod.uphub.data.database.model.User
import com.tariod.uphub.ui.base.navigation.UserProfileNavigator
import com.tariod.uphub.ui.main.profile.base.ProfileBaseListFragment
import com.tariod.uphub.utilities.ui.paging.AdapterHolder
import com.tariod.uphub.utilities.ui.paging.user.PagedUserAdapter

class ProfileFollowerFragment : ProfileBaseListFragment<User, ProfileFollowerViewModel>() {

    companion object {
        fun getInstance(userId: Int) = ProfileFollowerFragment().also {
            it.arguments = Bundle().also { bundle ->
                bundle.putInt(USER_ID, userId)
            }
        }
    }

    override val adapter: AdapterHolder<User> = PagedUserAdapter { userId ->
        activity.let {
            if (it is UserProfileNavigator)
                it.showUser(userId)
        }
    }

    override fun provideViewModel(): ProfileFollowerViewModel =
        ViewModelProvider(requireActivity(), viewModelFactory)[ProfileFollowerViewModel::class.java]
}