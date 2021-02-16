package com.chelinvest.notification.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.chelinvest.notification.R

class RoundedImageView : AppCompatImageView {

    constructor(context: Context) : super(context) { init(null) }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) { init(attrs) }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { init(attrs) }

    private val radius = FloatArray(8)
    private val path = Path()
    private val rect = RectF()

    private fun init(attrs: AttributeSet?) {
        if (attrs != null) {
            val typedArray =
                context.obtainStyledAttributes(attrs, R.styleable.RoundedImageView, 0, 0)
            try {
                val radiusAll =
                    typedArray.getDimension(R.styleable.RoundedImageView_radius, 0f)
                val radiusTopLeft =
                    typedArray.getDimension(R.styleable.RoundedImageView_radius_top_left, radiusAll)
                val radiusTopRight = typedArray.getDimension(
                    R.styleable.RoundedImageView_radius_top_right,
                    radiusAll
                )
                val radiusBottomLeft = typedArray.getDimension(
                    R.styleable.RoundedImageView_radius_bottom_left,
                    radiusAll
                )
                val radiusBottomRight = typedArray.getDimension(
                    R.styleable.RoundedImageView_radius_bottom_right,
                    radiusAll
                )
                radius[0] = radiusTopLeft
                radius[1] = radiusTopLeft
                radius[2] = radiusTopRight
                radius[3] = radiusTopRight
                radius[4] = radiusBottomRight
                radius[5] = radiusBottomRight
                radius[6] = radiusBottomLeft
                radius[7] = radiusBottomLeft
            } finally {
                typedArray.recycle()
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        if (canvas != null) {
            rect[0f, 0f, width.toFloat()] = height.toFloat()
            path.reset()
            path.addRoundRect(rect, radius, Path.Direction.CW)
            canvas.clipPath(path)
        }
        super.onDraw(canvas)
    }

}