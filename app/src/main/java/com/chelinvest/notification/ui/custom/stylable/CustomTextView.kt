package com.chelinvest.notification.ui.custom.stylable

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.chelinvest.notification.R

open class CustomTextView : AppCompatTextView {

    constructor(context: Context) : super(context) { init(null) }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) { init(attrs) }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { init(attrs) }

    private fun init(attrs: AttributeSet?) {
        if (attrs != null && !isInEditMode) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView, 0, 0)
            try {
                val style = typedArray.getInt(R.styleable.CustomTextView_customTypefaceStyle, 0)
                typeface = getTypefaceByStyle(context, style)
            } catch (_: Exception) {
            } finally {
                typedArray.recycle()
            }
        }
    }

    fun setRegular() {
        typeface = getTypefaceByStyle(context, TypefaceStyle.REGULAR)
    }

    fun setBold() {
        typeface = getTypefaceByStyle(context, TypefaceStyle.BOLD)
    }

}