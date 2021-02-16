package com.chelinvest.notification.ui.custom

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import com.chelinvest.notification.R
import com.chelinvest.notification.ui.custom.stylable.CustomTextView
import com.chelinvest.notification.additional.color
import com.chelinvest.notification.additional.string

class MoneyTextView : CustomTextView {

    constructor(context: Context) : super(context) { init(null) }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) { init(attrs) }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { init(attrs) }

    private fun init(attrs: AttributeSet?) {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MoneyTextView, 0, 0)
            try {
                style = typedArray.getInt(R.styleable.MoneyTextView_moneyTextViewMode, 0)
            } catch (_: Exception) {
            } finally {
                typedArray.recycle()
            }
        }
        update()
    }

    var style: Int = 0
        set(value) {
            field = value
            update()
        }

    var amount: Float = 0f
        set(value) {
            field = value
            update()
        }

    var fullAmount: Float? = null
        set(value) {
            field = value
            update()
        }

    private fun update() {
        text = when (style) {
            0 -> {
                setTextColor(context.color(
                        if (amount == 0f)
                            R.color.dark_gray
                        else if (amount > 0f) {
                            R.color.negative
                        } else {
                            R.color.positive
                        }
                ))
                var s = String.format("%.2f ${context.string(R.string.ruble)}", Math.abs(amount))
                if (amount < 0)
                    s = "+$s"
                if (amount > 0)
                    s = "-$s"
                s
            }
            1 -> {
                setTextColor(Color.WHITE)
                var s = String.format("%.2f ${context.string(R.string.ruble)}", Math.abs(amount))
                if (amount < 0)
                    s = "+$s"
                if (amount > 0)
                    s = "-$s"
                s
            }
            2 -> {
                setTextColor(context.color(R.color.tangelo))
                String.format("%.2f ${context.string(R.string.ruble)}", Math.abs(amount))
            }
            3 -> {
                setTextColor(context.color(R.color.dark_gray))
                String.format("%.2f ${context.string(R.string.ruble)}", Math.abs(amount))
            }
            4 -> {
                setTextColor(context.color(
                        if (amount == 0f)
                            R.color.dark_gray
                        else if (amount < 0f) {
                            R.color.negative
                        } else {
                            R.color.positive
                        }
                ))
                fullAmount?.let {
                    String.format("%.2f из %.2f ${context.string(R.string.ruble)}", amount, it)
                } ?: String.format("%.2f ${context.string(R.string.ruble)}", amount)
            }
            5 -> {
                setTextColor(context.color(R.color.dark_gray))
                String.format("%.2f ${context.string(R.string.ruble)}", amount)
            }
            else -> ""
        }
    }

}