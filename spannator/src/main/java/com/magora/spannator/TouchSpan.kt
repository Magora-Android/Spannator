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

import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.ClickableSpan

/**
 * Developed by Magora Team (magora-systems.com). 2017.
 */
abstract class TouchSpan(private val typeface: Typeface?, private val color: Int) : ClickableSpan() {

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.isUnderlineText = false
        if (typeface != null) {
            ds.typeface = typeface
        }
        ds.color = color
    }
}
