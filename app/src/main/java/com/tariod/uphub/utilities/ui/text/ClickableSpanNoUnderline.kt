package com.tariod.uphub.utilities.ui.text

import android.text.style.ClickableSpan
import android.view.View
import android.text.TextPaint

class ClickableSpanNoUnderline(val pos: Int, val callback: (Int) -> Unit) : ClickableSpan() {

    override fun onClick(widget: View) {
        callback(pos)
    }

    override fun updateDrawState(ds: TextPaint) {
        ds.isUnderlineText = false
    }
}