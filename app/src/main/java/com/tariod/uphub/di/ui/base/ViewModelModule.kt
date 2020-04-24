package com.tariod.uphub.di.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tariod.uphub.ui.explore.ExploreViewModel
import com.tariod.uphub.ui.explore.directory.DirectoryViewModel
import com.tariod.uphub.ui.login.LoginViewModel
import com.tariod.uphub.ui.main.MainViewModel
import com.tariod.uphub.ui.main.profile.ProfileViewModel
import com.tariod.uphub.ui.main.profile.follower.ProfileFollowerViewModel
import com.tariod.uphub.ui.main.profile.repository.ProfileRepositoryViewModel
import com.tariod.uphub.ui.main.profile.subscription.ProfileSubscriptionViewModel
import com.tariod.uphub.ui.main.repository.RepositoryViewModel
import com.tariod.uphub.ui.main.user.UserViewModel
import com.tariod.uphub.utilities.viewModel.ViewModelFactory
import com.tariod.uphub.utilities.viewModel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun mainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RepositoryViewModel::class)
    internal abstract fun repositoryViewModel(viewModel: RepositoryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    internal abstract fun profileViewModel(viewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    internal abstract fun loginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileSubscriptionViewModel::class)
    internal abstract fun profileSubscriptionViewModel(viewModel: ProfileSubscriptionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileRepositoryViewModel::class)
    internal abstract fun profileRepositoryViewModel(viewModel: ProfileRepositoryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileFollowerViewModel::class)
    internal abstract fun profileFollowingViewModel(viewModel: ProfileFollowerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel::class)
    internal abstract fun userViewModel(viewModel: UserViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DirectoryViewModel::class)
    internal abstract fun directoryViewModel(directoryViewModel: DirectoryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExploreViewModel::class)
    internal abstract fun exploreViewModel(exploreViewModel: ExploreViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}