package com.tariod.uphub.utilities.ui.list.dir

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tariod.uphub.R
import com.tariod.uphub.data.database.model.DirectoryItem
import com.tariod.uphub.databinding.ItemDirBinding
import com.tariod.uphub.ui.base.BaseRecyclerAdapter
import com.tariod.uphub.ui.base.BaseViewHolder
import com.tariod.uphub.utilities.MetricsUtil
import com.tariod.uphub.utilities.ui.list.dir.model.DirUI

class DirAdapter(val navigateToDir: (DirectoryItem) -> Unit) :
    BaseRecyclerAdapter<DirHolder, DirUI>() {

    override fun provideHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): DirHolder =
        DirHolder(ItemDirBinding.inflate(layoutInflater, parent, false), navigateToDir)
}

class DirHolder(val binding: ItemDirBinding, val navigateToDir: (DirectoryItem) -> Unit) :
    BaseViewHolder<DirUI>(binding.root) {

    override fun bind(model: DirUI) {
        model.dir?.let { item ->
            binding.text.text = item.name
            binding.icon.setImageResource(if (item.isFolder) R.drawable.ic_dir else R.drawable.ic_file)
            if (item.isFolder) {
                binding.container.setOnClickListener { navigateToDir(item) }
                binding.size.visibility = View.GONE
            } else {
                binding.size.text = MetricsUtil.formatBytes(item.size, true)
                binding.size.visibility = View.VISIBLE
            }
        }
    }
}