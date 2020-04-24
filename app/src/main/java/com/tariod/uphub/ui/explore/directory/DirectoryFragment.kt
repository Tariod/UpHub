package com.tariod.uphub.ui.explore.directory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tariod.uphub.databinding.FragmentDirBinding
import com.tariod.uphub.ui.base.BaseFragment
import com.tariod.uphub.ui.explore.NewDirNavigator
import com.tariod.uphub.utilities.livedata.observe
import com.tariod.uphub.utilities.ui.list.dir.DirAdapter

class DirectoryFragment : BaseFragment<DirectoryViewModel, FragmentDirBinding>() {

    companion object {

        private const val REPOSITORY_ID = "REPOSITORY_ID"
        private const val SHA = "SHA"

        fun getInstance(reposId: Int, sha: String, name: String) = DirectoryFragment().also {
            it.arguments = Bundle().also {
                it.putInt(REPOSITORY_ID, reposId)
                it.putString(SHA, sha)
            }
        }
    }

    lateinit var sha: String

    private val adapter = DirAdapter { (activity as? NewDirNavigator)?.navigateToNewDir(sha, it) }

    override fun onBinding(bind: FragmentDirBinding) {
        val reposId = arguments?.getInt(REPOSITORY_ID) ?: -1
        sha = arguments?.getString(SHA) ?: ""

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.list.apply {
            recycler.adapter = adapter
            recycler.layoutManager = LinearLayoutManager(context)
            recycler.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
        binding.list.onSwipe = { viewModel.onFetchDirectory(reposId, sha) }

        observe(viewModel.models) { adapter.update(it) }

        viewModel.onFetchDirectory(reposId, sha)
    }

    override fun provideBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDirBinding =
        FragmentDirBinding.inflate(inflater, container, false)

    override fun provideViewModel(): DirectoryViewModel =
        ViewModelProvider(this, viewModelFactory)[DirectoryViewModel::class.java]
}