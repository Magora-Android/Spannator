/*
    Spannable library.
    Copyright (C) 2019  Magora Systems.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.magora.spannator

import android.content.res.Resources
import android.graphics.Color
import android.graphics.Typeface
import android.text.Annotation
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannedString
import android.text.style.*
import android.util.TypedValue
import android.view.View

/**
 * Developed by Magora Team (magora-systems.com). 2017.
 */
const val BG_COLOR = "backgroundColor"
const val TEXT_COLOR = "textColor"
const val URL = "url"
const val TEXT_SIZE_ABSOLUTE = "textSizePx"
const val TEXT_SIZE_SP = "textSizeSp"
const val TYPEFACE = "typeface"
const val TYPEFACE_BOLD = "bold"
const val TYPEFACE_NORMAL = "normal"
const val TYPEFACE_MONO = "monospace"
const val CLICKABLE = "clickable"

class Spannee(
    private val res: Resources,
    private val typefaceProvider: ((String) -> Typeface)? = null
) {
    operator fun invoke(spanId: Int, onClick: ((String) -> Unit)? = null): SpannableString {
        val text = SpannableString(res.getText(spanId) as SpannedString)
        val annotations = text.getSpans(0, text.length, Annotation::class.java)
        for (annotation in annotations) {
            when (annotation.key) {
                BG_COLOR -> setBackgroundColor(
                    annotation.value,
                    text,
                    text.getSpanStart(annotation),
                    text.getSpanEnd(annotation)
                )
                TEXT_COLOR -> setTextColor(
                    annotation.value,
                    text,
                    text.getSpanStart(annotation),
                    text.getSpanEnd(annotation)
                )
                URL -> setClickableLink(
                    annotation.value,
                    text,
                    text.getSpanStart(annotation),
                    text.getSpanEnd(annotation)
                )
                TEXT_SIZE_ABSOLUTE -> setTextSizePx(
                    annotation.value.toInt(),
                    text,
                    text.getSpanStart(annotation),
                    text.getSpanEnd(annotation)
                )
                TEXT_SIZE_SP -> setTextSizeDp(
                    annotation.value.toFloat().toSp(),
                    text,
                    text.getSpanStart(annotation),
                    text.getSpanEnd(annotation)
                )
                TYPEFACE -> setTypeface(
                    annotation.value,
                    text,
                    text.getSpanStart(annotation),
                    text.getSpanEnd(annotation)
                )
                CLICKABLE -> {
                    if (onClick != null) {
                        setClickable(
                            annotation.value,
                            text,
                            text.getSpanStart(annotation),
                            text.getSpanEnd(annotation),
                            onClick
                        )
                    }
                }
            }
        }
        return text
    }

    private fun setBackgroundColor(color: String, text: SpannableString, start: Int, end: Int) {
        text.setSpan(BackgroundColorSpan(Color.parseColor(color)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    private fun setTextColor(color: String, text: SpannableString, start: Int, end: Int) {
        text.setSpan(ForegroundColorSpan(Color.parseColor(color)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    private fun setClickableLink(link: String, text: SpannableString, start: Int, end: Int) {
        text.setSpan(URLSpan(link), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    private fun setTextSizePx(size: Int, text: SpannableString, start: Int, end: Int) {
        text.setSpan(AbsoluteSizeSpan(size, true), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    private fun setTextSizeDp(size: Int, text: SpannableString, start: Int, end: Int) {
        text.setSpan(AbsoluteSizeSpan(size, false), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    private fun setTypeface(typefaceName: String, text: SpannableString, start: Int, end: Int) {
        if (typefaceProvider != null) {
            text.setSpan(
                CustomTypefaceSpan(typefaceProvider.invoke(typefaceName)),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        } else {
            when (typefaceName) {
                TYPEFACE_BOLD -> text.setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                TYPEFACE_NORMAL -> text.setSpan(
                    StyleSpan(Typeface.NORMAL),
                    start,
                    end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                TYPEFACE_MONO -> text.setSpan(
                    CustomTypefaceSpan(Typeface.MONOSPACE),
                    start,
                    end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
    }

    private fun setClickable(
        key: String,
        text: SpannableString,
        start: Int,
        end: Int,
        onClickCallback: (String) -> Unit
    ) {
        text.setSpan(object : ClickableSpan() {

            override fun onClick(widget: View) {
                onClickCallback(key)
            }
        }, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
}

private fun Float.toSp() =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, Resources.getSystem().displayMetrics).toInt()