/*
 * Copyright (C) 2011 Micah Hainline
 * Copyright (C) 2012 Triposo
 * Copyright (C) 2013 Paul Imhoff
 * Copyright (C) 2014 Shahin Yousefi
 * Copyright (C) 2015 Stepan Goncharov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tariod.uphub.utilities.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.Layout;
import android.text.Layout.Alignment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A {@link android.widget.TextView} that ellipsizes more intelligently.
 * This class supports ellipsizing multiline text through setting {@code android:ellipsize}
 * and {@code android:maxLines}.
 * <p/>
 * Note: {@link TruncateAt#MARQUEE} ellipsizing type is not supported.
 */
public class DirectoryPathView extends AppCompatTextView {
    public static final int ELLIPSIZE_ALPHA = 0x88;
    private SpannableString ELLIPSIS = new SpannableString("\u2026");

    private static final Pattern DEFAULT_END_PUNCTUATION
            = Pattern.compile("[\\.!?,;:\u2026]*$", Pattern.DOTALL);
    private final List<EllipsizeListener> mEllipsizeListeners = new ArrayList<>();
    private EllipsizeStrategy mEllipsizeStrategy;
    private boolean isEllipsized;
    private boolean isStale;
    private boolean programmaticChange;
    private CharSequence mFullText;
    private int mMaxLines = 1;
    private float mLineSpacingMult = 1.0f;
    private float mLineAddVertPad = 0.0f;

    /**
     * The end punctuation which will be removed when appending {@link #ELLIPSIS}.
     */
    private Pattern mEndPunctPattern;

    public DirectoryPathView(Context context) {
        this(context, null);
    }


    public DirectoryPathView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public DirectoryPathView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs,
                new int[]{android.R.attr.maxLines, android.R.attr.ellipsize}, defStyle, 0);
        setMaxLines(a.getInt(0, 1));
        a.recycle();
        setEndPunctuationPattern(DEFAULT_END_PUNCTUATION);
        final int currentTextColor = getCurrentTextColor();
        final int ellipsizeColor = Color.argb(ELLIPSIZE_ALPHA, Color.red(currentTextColor), Color.green(currentTextColor), Color.blue(currentTextColor));
        ELLIPSIS.setSpan(new ForegroundColorSpan(ellipsizeColor), 0, ELLIPSIS.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public void setEndPunctuationPattern(Pattern pattern) {
        mEndPunctPattern = pattern;
    }

    @SuppressWarnings("unused")
    public void addEllipsizeListener(@NonNull EllipsizeListener listener) {
        mEllipsizeListeners.add(listener);
    }

    @SuppressWarnings("unused")
    public void removeEllipsizeListener(@NonNull EllipsizeListener listener) {
        mEllipsizeListeners.remove(listener);
    }

    @SuppressWarnings("unused")
    public boolean isEllipsized() {
        return isEllipsized;
    }

    /**
     * @return The maximum number of lines displayed in this {@link android.widget.TextView}.
     */
    public int getMaxLines() {
        return mMaxLines;
    }

    @Override
    public void setMaxLines(int maxLines) {
        super.setMaxLines(maxLines);
        mMaxLines = maxLines;
        isStale = true;
    }

    /**
     * Determines if the last fully visible line is being ellipsized.
     *
     * @return {@code true} if the last fully visible line is being ellipsized;
     * otherwise, returns {@code false}.
     */
    public boolean ellipsizingLastFullyVisibleLine() {
        return mMaxLines == Integer.MAX_VALUE;
    }

    @Override
    public void setLineSpacing(float add, float mult) {
        mLineAddVertPad = add;
        mLineSpacingMult = mult;
        super.setLineSpacing(add, mult);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (!programmaticChange) {
            mFullText = text instanceof Spanned ? (Spanned) text : text;
            isStale = true;
        }
        super.setText(text, type);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (ellipsizingLastFullyVisibleLine()) {
            isStale = true;
        }
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        if (ellipsizingLastFullyVisibleLine()) {
            isStale = true;
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        if (isStale) {
            resetText();
        }
        super.onDraw(canvas);
    }

    /**
     * Sets the ellipsized text if appropriate.
     */
    private void resetText() {
        int maxLines = getMaxLines();
        CharSequence workingText = mFullText;
        boolean ellipsized = false;

        if (maxLines != -1) {
            if (mEllipsizeStrategy == null) setEllipsize(null);
            workingText = mEllipsizeStrategy.processText(mFullText);
            ellipsized = !mEllipsizeStrategy.isInLayout(mFullText);
        }

        if (!workingText.equals(getText())) {
            programmaticChange = true;
            try {
                setText(workingText);
            } finally {
                programmaticChange = false;
            }
        }

        isStale = false;
        if (ellipsized != isEllipsized) {
            isEllipsized = ellipsized;
            for (EllipsizeListener listener : mEllipsizeListeners) {
                listener.ellipsizeStateChanged(ellipsized);
            }
        }
    }

    /**
     * Causes words in the text that are longer than the view is wide to be ellipsized
     * instead of broken in the middle. Use {@code null} to turn off ellipsizing.
     * <p/>
     * Note: Method does nothing for {@link TruncateAt#MARQUEE}
     * ellipsizing type.
     *
     * @param where part of text to ellipsize
     */
    @Override
    public void setEllipsize(TruncateAt where) {
        mEllipsizeStrategy = new EllipsizeStartStrategy();
    }

    /**
     * A listener that notifies when the ellipsize state has changed.
     */
    public interface EllipsizeListener {
        void ellipsizeStateChanged(boolean ellipsized);
    }

    /**
     * A base class for an ellipsize strategy.
     */
    private abstract class EllipsizeStrategy {
        /**
         * Returns ellipsized text if the text does not fit inside of the layout;
         * otherwise, returns the full text.
         *
         * @param text text to process
         * @return Ellipsized text if the text does not fit inside of the layout;
         * otherwise, returns the full text.
         */
        public CharSequence processText(CharSequence text) {
            return !isInLayout(text) ? createEllipsizedText(text) : text;
        }

        /**
         * Determines if the text fits inside of the layout.
         *
         * @param text text to fit
         * @return {@code true} if the text fits inside of the layout;
         * otherwise, returns {@code false}.
         */
        public boolean isInLayout(CharSequence text) {
            Layout layout = createWorkingLayout(text);
            return layout.getLineCount() <= getLinesCount();
        }

        /**
         * Creates a working layout with the given text.
         *
         * @param workingText text to create layout with
         * @return {@link Layout} with the given text.
         */
        protected Layout createWorkingLayout(CharSequence workingText) {
            return new StaticLayout(workingText, getPaint(),
                    getWidth() - getCompoundPaddingLeft() - getCompoundPaddingRight(),
                    Alignment.ALIGN_NORMAL, mLineSpacingMult,
                    mLineAddVertPad, false /* includepad */);
        }

        /**
         * Get how many lines of text we are allowed to display.
         */
        protected int getLinesCount() {
            if (ellipsizingLastFullyVisibleLine()) {
                int fullyVisibleLinesCount = getFullyVisibleLinesCount();
                return fullyVisibleLinesCount == -1 ? 1 : fullyVisibleLinesCount;
            } else {
                return mMaxLines;
            }
        }

        /**
         * Get how many lines of text we can display so their full height is visible.
         */
        protected int getFullyVisibleLinesCount() {
            Layout layout = createWorkingLayout("");
            int height = getHeight() - getCompoundPaddingTop() - getCompoundPaddingBottom();
            int lineHeight = layout.getLineBottom(0);
            return height / lineHeight;
        }

        /**
         * Creates ellipsized text from the given text.
         *
         * @param fullText text to ellipsize
         * @return Ellipsized text
         */
        protected abstract CharSequence createEllipsizedText(CharSequence fullText);
    }

    /**
     * An {@link EllipsizeStrategy} that
     * ellipsizes text at the start.
     */
    private class EllipsizeStartStrategy extends EllipsizeStrategy {
        @Override
        protected CharSequence createEllipsizedText(CharSequence fullText) {
            Layout layout = createWorkingLayout(fullText);
            int cutOffIndex = layout.getLineEnd(mMaxLines - 1);
            int textLength = fullText.length();
            int cutOffLength = textLength - cutOffIndex;
            if (cutOffLength < ELLIPSIS.length()) cutOffLength = ELLIPSIS.length();
            int cuffOffPos = Math.max(TextUtils.indexOf(fullText, '/', cutOffLength), 0);
            CharSequence workingText = TextUtils.substring(fullText, cuffOffPos, textLength).trim();

            while (!isInLayout(TextUtils.concat(ELLIPSIS, workingText))) {
                int firstSpace = TextUtils.indexOf(workingText, ' ');
                if (firstSpace == -1) {
                    break;
                }
                workingText = TextUtils.substring(workingText, firstSpace, workingText.length()).trim();
            }

            workingText = TextUtils.concat(ELLIPSIS, workingText);
            SpannableStringBuilder dest = new SpannableStringBuilder(workingText);

            if (fullText instanceof Spanned) {
                TextUtils.copySpansFrom((Spanned) fullText, textLength - workingText.length(),
                        textLength, null, dest, 0);
            }
            return dest;
        }
    }
}