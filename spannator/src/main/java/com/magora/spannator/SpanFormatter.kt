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

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.SpannedString
import java.util.*
import java.util.regex.Pattern

/**
 * Developed by Magora Team (magora-systems.com). 2017.
 */
object SpanFormatter {
    private val FORMAT_SEQUENCE = Pattern.compile("%([0-9]+\\$|<?)([^a-zA-z%]*)([[a-zA-Z%]&&[^tT]]|[tT][a-zA-Z])")

    /**
     * Version of [String.format] that works on [Spanned] strings to preserve rich text formatting.
     * Both the `format` as well as any `%s args` can be Spanned and will have their formatting preserved.
     * Due to the way [Spannable]s work, any argument's spans will can only be included **once** in the result.
     * Any duplicates will appear as text only.
     *
     * @param format the format string (see [java.util.Formatter.format])
     * @param args   the list of arguments passed to the formatter. If there are
     * more arguments than required by `format`,
     * additional arguments are ignored.
     * @return the formatted string (with spans).
     */
    fun format(format: CharSequence, vararg args: Any): SpannedString =
        format(Locale.getDefault(), format, *args)

    /**
     * Version of [String.format] that works on [Spanned] strings to preserve rich text formatting.
     * Both the `format` as well as any `%s args` can be Spanned and will have their formatting preserved.
     * Due to the way [Spannable]s work, any argument's spans will can only be included **once** in the result.
     * Any duplicates will appear as text only.
     *
     * @param locale the locale to apply; `null` value means no localization.
     * @param format the format string (see [java.util.Formatter.format])
     * @param args   the list of arguments passed to the formatter.
     * @return the formatted string (with spans).
     * @see String.format
     */
    fun format(locale: Locale, format: CharSequence, vararg args: Any): SpannedString {
        val out = SpannableStringBuilder(format)

        var i = 0
        var argAt = -1

        while (i < out.length) {
            val m = FORMAT_SEQUENCE.matcher(out)
            if (!m.find(i)) break
            i = m.start()
            val exprEnd = m.end()

            val argTerm = m.group(1)
            val modTerm = m.group(2)
            val typeTerm = m.group(3)

            val cookedArg: CharSequence

            when (typeTerm) {
                "%" -> cookedArg = "%"
                "n" -> cookedArg = "\n"
                else -> {
                    val argIdx: Int = when {
                        argTerm.isEmpty() -> ++argAt
                        "<" == argTerm -> argAt
                        else -> Integer.parseInt(argTerm.substring(0, argTerm.length - 1)) - 1
                    }

                    val argItem = args[argIdx]

                    cookedArg = if ("s" == typeTerm && argItem is Spanned) {
                        argItem
                    } else {
                        String.format(locale, "%$modTerm$typeTerm", argItem)
                    }
                }
            }

            out.replace(i, exprEnd, cookedArg)
            i += cookedArg.length
        }

        return SpannedString(out)
    }
}
