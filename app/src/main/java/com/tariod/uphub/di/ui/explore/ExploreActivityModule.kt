package com.tariod.uphub.di.ui.explore

import com.tariod.uphub.ui.explore.directory.DirectoryFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ExploreActivityModule {

    @ContributesAndroidInjector
    abstract fun bindDirectoryFragment(): DirectoryFragment
}