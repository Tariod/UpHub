package com.tariod.uphub.utilities.ui

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.tariod.uphub.R
import com.tariod.uphub.utilities.ui.view.ProgressLayout
import com.tariod.uphub.utilities.ui.view.ProgressRecyclerView

@BindingAdapter("progress")
fun bindProgress(progressLayout: ProgressLayout, progress: Boolean) {
    if (progress) progressLayout.show() else progressLayout.hide()
}

@BindingAdapter("progress")
fun bindProgressList(progressLayout: ProgressRecyclerView, progress: Boolean) {
    if (progress) progressLayout.showProgress() else progressLayout.hideProgress()
}

@BindingAdapter("imageUrl")
fun bindImageUrl(imageView: ImageView, url: String?) {
    url?.takeIf {
        it.isNotBlank()
    }?.let { url ->
        Glide.with(imageView)
            .load(url)
            .placeholder(R.drawable.ic_account)
            .circleCrop()
            .into(imageView)
    } ?: run {
        imageView.setImageResource(R.drawable.ic_account)
    }
}