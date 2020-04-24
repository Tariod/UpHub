package com.tariod.uphub.ui.main.profile.subscription

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.tariod.uphub.data.database.model.User
import com.tariod.uphub.ui.base.navigation.UserProfileNavigator
import com.tariod.uphub.ui.main.profile.base.ProfileBaseListFragment
import com.tariod.uphub.utilities.ui.paging.AdapterHolder
import com.tariod.uphub.utilities.ui.paging.user.PagedUserAdapter

class ProfileSubscriptionFragment : ProfileBaseListFragment<User, ProfileSubscriptionViewModel>() {

    companion object {
        private const val USER_ID = "user_id"

        fun getInstance(userId: Int) = ProfileSubscriptionFragment().also {
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

    override fun provideViewModel(): ProfileSubscriptionViewModel = ViewModelProvider(
        requireActivity(),
        viewModelFactory
    )[ProfileSubscriptionViewModel::class.java]
}