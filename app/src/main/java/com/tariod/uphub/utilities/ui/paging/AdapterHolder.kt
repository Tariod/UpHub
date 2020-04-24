package com.tariod.uphub.utilities.ui.paging

import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView

interface AdapterHolder<M> {

    fun onList(list: PagedList<M>)

    fun injectIntoRecycler(recyclerView: RecyclerView)
}