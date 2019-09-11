package com.pryanya.summerwallet.utils

import android.text.Spanned
import android.text.InputFilter
import java.util.regex.Pattern
import android.text.TextUtils


class DecimalDigitsInputFilter(digitsBeforeZero: Int = 8, digitsAfterZero: Int = 2) : InputFilter {
    private val mPattern = Pattern.compile(
        "[0-9]{0," + digitsBeforeZero + "}+((\\.[0-9]{0," + digitsAfterZero
                + "})?)||(\\.)?"
    )
    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        val replacement = source.subSequence(start, end).toString()
        val newVal = (dest.subSequence(0, dstart).toString() + replacement
                + dest.subSequence(dend, dest.length).toString())
        val matcher = mPattern.matcher(newVal)
        if (matcher.matches())
            return null

        return if (TextUtils.isEmpty(source))
            dest.subSequence(dstart, dend)
        else
            ""
    }
}