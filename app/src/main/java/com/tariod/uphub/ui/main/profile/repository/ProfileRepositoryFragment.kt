package com.tariod.uphub.ui.main.profile.repository

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.tariod.uphub.data.database.model.Repository
import com.tariod.uphub.ui.explore.ExploreActivity
import com.tariod.uphub.ui.main.profile.base.ProfileBaseListFragment
import com.tariod.uphub.utilities.ui.paging.AdapterHolder
import com.tariod.uphub.utilities.ui.paging.repos.PagedRepositoryAdapter

class ProfileRepositoryFragment :
    ProfileBaseListFragment<Repository, ProfileRepositoryViewModel>() {

    companion object {

        private const val USER_ID = "user_id"

        fun getInstance(userId: Int) = ProfileRepositoryFragment().also {
            it.arguments = Bundle().also { bundle ->
                bundle.putInt(USER_ID, userId)
            }
        }
    }

    override val adapter: AdapterHolder<Repository> = PagedRepositoryAdapter { onExplore(it) }

    private fun onExplore(repos: Repository) =
        startActivity(ExploreActivity.getIntent(requireContext(), repos.id, repos.name))

    override fun provideViewModel(): ProfileRepositoryViewModel =
        ViewModelProvider(this, viewModelFactory)[ProfileRepositoryViewModel::class.java]
}