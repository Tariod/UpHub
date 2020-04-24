package com.tariod.uphub.utilities.ui.list.repos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tariod.uphub.data.database.model.Repository
import com.tariod.uphub.databinding.ItemProgressBinding
import com.tariod.uphub.databinding.ItemRepositoryBinding
import com.tariod.uphub.ui.base.BaseRecyclerAdapter
import com.tariod.uphub.ui.base.BaseViewHolder
import com.tariod.uphub.utilities.ui.list.repos.model.RepositoryUI

class RepositoryAdapter(val onExplore: (Repository) -> Unit) :
    BaseRecyclerAdapter<RepositoryUIViewHolder, RepositoryUI>() {

    companion object {

        private const val TYPE_REPOSITORY = 45
        private const val TYPE_PROGRESS = 23
    }

    override fun provideHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): RepositoryUIViewHolder = when (viewType) {
        TYPE_REPOSITORY ->
            RepositoryViewHolder(
                ItemRepositoryBinding.inflate(layoutInflater, parent, false),
                onExplore
            )
        else ->
            EmptyViewHolder(ItemProgressBinding.inflate(layoutInflater, parent, false))
    }

    override fun getItemViewType(position: Int): Int =
        if (getItemAt(position).repository == null) TYPE_PROGRESS else TYPE_REPOSITORY
}

class RepositoryViewHolder(
    private val binding: ItemRepositoryBinding,
    val onExplore: (Repository) -> Unit
) : RepositoryUIViewHolder(binding.root) {

    override fun bind(model: RepositoryUI) {
        binding.repos = model.repository
        binding.container.setOnClickListener {
            onExplore(model.repository ?: return@setOnClickListener)
        }
        binding.executePendingBindings()
    }
}

class EmptyViewHolder(binding: ItemProgressBinding) : RepositoryUIViewHolder(binding.root)

abstract class RepositoryUIViewHolder(view: View) : BaseViewHolder<RepositoryUI>(view)