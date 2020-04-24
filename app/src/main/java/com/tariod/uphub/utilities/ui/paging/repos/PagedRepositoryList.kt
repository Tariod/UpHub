package com.tariod.uphub.utilities.ui.paging.repos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tariod.uphub.data.database.model.Repository
import com.tariod.uphub.databinding.ItemRepositoryBinding
import com.tariod.uphub.ui.base.BaseViewHolder
import com.tariod.uphub.ui.base.RepositoryDiffUtilCallback
import com.tariod.uphub.utilities.ui.paging.AdapterHolder

class PagedRepositoryAdapter(private val navigateToRepos: (Repository) -> Unit) :
    PagedListAdapter<Repository, PagedRepositoryViewHolder>(RepositoryDiffUtilCallback()),
    AdapterHolder<Repository> {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagedRepositoryViewHolder =
        PagedRepositoryViewHolder(
            ItemRepositoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), navigateToRepos
        )

    override fun onBindViewHolder(holder: PagedRepositoryViewHolder, position: Int) {
        holder.bind(getItem(position) ?: return)
    }

    override fun onList(list: PagedList<Repository>) = submitList(list)

    override fun injectIntoRecycler(recyclerView: RecyclerView) {
        recyclerView.adapter = this
    }
}

class PagedRepositoryViewHolder(
    private val binding: ItemRepositoryBinding,
    private val navigateToRepos: (Repository) -> Unit
) : BaseViewHolder<Repository>(binding.root) {

    override fun bind(model: Repository) {
        binding.repos = model
        binding.container.setOnClickListener { navigateToRepos(model) }
        binding.executePendingBindings()
    }
}