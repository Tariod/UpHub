package com.tariod.uphub.ui.main.profile

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.tariod.uphub.R
import com.tariod.uphub.databinding.FragmentProfileBinding
import com.tariod.uphub.ui.base.BaseFragment
import com.tariod.uphub.ui.login.LoginActivity
import com.tariod.uphub.utilities.livedata.observe
import com.tariod.uphub.utilities.ui.str
import com.tariod.uphub.ui.main.profile.follower.ProfileFollowerFragment
import com.tariod.uphub.ui.main.profile.repository.ProfileRepositoryFragment
import com.tariod.uphub.ui.main.profile.subscription.ProfileSubscriptionFragment

class ProfileFragment : BaseFragment<ProfileViewModel, FragmentProfileBinding>() {

    private val titles =
        listOf(R.string.profile_repos, R.string.profile_follow, R.string.profile_subscr)
    private lateinit var adapter: ProfilePagerAdapter

    override fun onBinding(bind: FragmentProfileBinding) {
        super.onBinding(bind)

        val userId = arguments?.getInt("userId") ?: -1

        adapter = ProfilePagerAdapter(
            listOf(
                ProfileRepositoryFragment.getInstance(userId),
                ProfileSubscriptionFragment.getInstance(userId),
                ProfileFollowerFragment.getInstance(userId)
            ), childFragmentManager
        )
        bind.viewModel = viewModel
        bind.pager.adapter = adapter
        bind.pager.offscreenPageLimit = 3
        bind.tabs.setupWithViewPager(bind.pager)
        bind.lifecycleOwner = this
        bind.back.setOnClickListener { findNavController().navigateUp() }
        if (userId == -1) bind.back.visibility = View.GONE

        observe(viewModel.navigateToSignIn) {
            startActivity(LoginActivity.getIntent(requireContext()))
        }
        observe(viewModel.navigateToSignOut) {
            showSignOutDialog()
        }

        observe(viewModel.user) { user ->
            listOf(user.publicRepos, user.following, user.followers).forEachIndexed { index, i ->
                bind.tabs.getTabAt(index)?.text = str(titles[index], i)
            }
        }

        viewModel.onUser(userId.takeIf { it != -1 })
    }

    private fun showSignOutDialog() {
        AlertDialog.Builder(context)
            .setTitle(R.string.common_warning)
            .setMessage(R.string.profile_exit_confirm)
            .setPositiveButton(R.string.common_yes) { _, _ -> viewModel.signOut() }
            .setNegativeButton(R.string.common_no) { _, _ -> }
            .create()
            .show()
    }

    override fun provideViewModel(): ProfileViewModel =
        ViewModelProvider(requireActivity(), viewModelFactory)[ProfileViewModel::class.java]

    override fun provideBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfileBinding = FragmentProfileBinding.inflate(inflater, container, false)
}