package com.tariod.uphub.di.ui.main

import com.tariod.uphub.ui.explore.directory.DirectoryFragment
import com.tariod.uphub.ui.main.profile.ProfileFragment
import com.tariod.uphub.ui.main.repository.RepositoryFragment
import com.tariod.uphub.ui.main.user.UserFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {

    @ContributesAndroidInjector
    abstract fun bindRepositoryFragment(): RepositoryFragment

    @ContributesAndroidInjector(modules = [ProfileFragmentModule::class])
    abstract fun bindProfileFragment(): ProfileFragment

    @ContributesAndroidInjector
    abstract fun bindUserFragment(): UserFragment

    @ContributesAndroidInjector
    abstract fun bindExploreFragment(): DirectoryFragment

}