package com.tariod.uphub.ui.main

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.tariod.uphub.R
import com.tariod.uphub.databinding.ActivityMainBinding
import com.tariod.uphub.ui.base.BaseActivity
import com.tariod.uphub.ui.base.navigation.UserProfileNavigator
import com.tariod.uphub.ui.login.LoginActivity
import com.tariod.uphub.utilities.livedata.observe

class MainActivity : BaseActivity<MainViewModel>(), UserProfileNavigator {

    override val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observe(viewModel.navigateToLogin) { startActivity(LoginActivity.getIntent(this)) }

        binding.bottomNavigation.setupWithNavController(
            Navigation.findNavController(
                this,
                R.id.host
            )
        )
    }

    override fun onStart() {
        super.onStart()
        viewModel.checkNavigation()
    }

    override fun showUser(userId: Int) {
        findNavController(R.id.host).navigate(R.id.action_to_profile_view, Bundle().also {
            it.putInt("userId", userId)
        })
    }

    override fun provideViewModel() =
        ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
}