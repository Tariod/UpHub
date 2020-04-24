package com.tariod.uphub.ui.main.profile.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tariod.uphub.databinding.FragmentProfileListBinding
import com.tariod.uphub.ui.base.BaseFragment
import com.tariod.uphub.utilities.livedata.observe
import com.tariod.uphub.utilities.ui.paging.AdapterHolder

abstract class ProfileBaseListFragment<M, VM : ProfileBaseListViewModel<M>> :
    BaseFragment<VM, FragmentProfileListBinding>() {

    companion object {
        val USER_ID = "user_id"
    }

    abstract val adapter: AdapterHolder<M>

    private lateinit var dividerItemDecoration: DividerItemDecoration

    override fun onBinding(bind: FragmentProfileListBinding) {
        super.onBinding(bind)
        bind.viewModel = viewModel
        bind.lifecycleOwner = this
        bind.list.apply {
            adapter.injectIntoRecycler(recycler)
            LinearLayoutManager(context).let {
                dividerItemDecoration = DividerItemDecoration(context, it.orientation)
                recycler.layoutManager = it
            }
            recycler.addItemDecoration(dividerItemDecoration)
        }
        bind.list.onSwipe = {
            viewModel.onFetch(arguments?.getInt(USER_ID).takeIf { it != -1 })
        }
        observe(viewModel.models) { adapter.onList(it) }

        viewModel.onFetch(arguments?.getInt(USER_ID).takeIf { it != -1 })
    }

    override fun onDestroyView() {
        binding.list.apply {
            recycler.layoutManager = null
            recycler.adapter = null
            recycler.removeItemDecoration(dividerItemDecoration)
        }
        super.onDestroyView()
    }

    override fun provideBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfileListBinding = FragmentProfileListBinding.inflate(inflater, container, false)
}