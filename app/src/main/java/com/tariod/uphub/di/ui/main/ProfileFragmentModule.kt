package com.tariod.uphub.di.ui.main

import com.tariod.uphub.ui.main.profile.follower.ProfileFollowerFragment
import com.tariod.uphub.ui.main.profile.repository.ProfileRepositoryFragment
import com.tariod.uphub.ui.main.profile.subscription.ProfileSubscriptionFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ProfileFragmentModule {

    @ContributesAndroidInjector
    abstract fun bindProfileSubscriptionFragment(): ProfileSubscriptionFragment

    @ContributesAndroidInjector
    abstract fun bindProfileRepositoryFragment(): ProfileRepositoryFragment

    @ContributesAndroidInjector
    abstract fun bindProfileFollowerFragment(): ProfileFollowerFragment
}