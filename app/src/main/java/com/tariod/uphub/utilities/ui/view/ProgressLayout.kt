package com.tariod.uphub.utilities.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.annotation.ColorInt
import com.tariod.uphub.R
import com.tariod.uphub.utilities.ui.col

class ProgressLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    companion object {

        private const val DURATION = 400L
    }

    private val dim = View(context).apply {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        setBackgroundColor(col(R.color.progressDim))
        alpha = 0F
        visibility = View.GONE
    }

    private val progress = ProgressBar(context).apply {
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            gravity = Gravity.CENTER
        }
        alpha = 0F
        visibility = View.GONE
    }

    private val content = FrameLayout(context).apply {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }

    private var shown = false

    init {
        addView(content)
        addView(dim)
        addView(progress)
    }

    fun setDimColor(@ColorInt color: Int) = dim.setBackgroundColor(color)

    fun show() {
        if (shown) return
        dim.isClickable = true
        dim.visibility = View.VISIBLE
        progress.visibility = View.VISIBLE
        dim.animate()
            .alpha(1F).duration = DURATION
        progress.animate()
            .alpha(1F).duration = DURATION
        content.apply {
            isClickable = false
            isFocusable = false
        }
        shown = true
    }

    fun hide() {
        if (!shown) return
        dim.isClickable = false
        dim.animate()
            .alpha(0F)
            .setDuration(DURATION)
            .withEndAction {
                dim.visibility = View.GONE
            }
        progress.animate()
            .alpha(0F)
            .setDuration(DURATION)
            .withEndAction {
                progress.visibility = View.GONE
            }
        shown = false
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        if (child == content || child == dim || child == progress)
            super.addView(child, index, params)
        else
            content.addView(child, index, params)
    }
}