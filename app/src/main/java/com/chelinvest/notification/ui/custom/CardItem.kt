package com.chelinvest.notification.ui.custom

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.chelinvest.notification.R

class CardItem: FrameLayout {

    private var textView: TextView? = null
    private var iconImageView: ImageView? = null
    private var selectedStyleLayout: View? = null
    private var selectedColor = 0
    private var unselectedColor = 0

    constructor(context: Context?) : super(context!!) { init(null) }
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) { init(attrs) }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context!!, attrs, defStyleAttr) { init(attrs) }

    fun setIsSelected(isSelected: Boolean) {
        if (isSelected) {
            textView?.setTextColor(selectedColor)
            iconImageView?.setColorFilter(selectedColor)
            selectedStyleLayout?.setBackgroundResource(R.drawable.menu_selected_item)
        } else {
            textView?.setTextColor(unselectedColor)
            iconImageView?.setColorFilter(unselectedColor)
            selectedStyleLayout?.setBackgroundResource(R.drawable.menu_unselected_item)
        }
    }

    private fun init(attrs: AttributeSet?) {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.card_item_layout, this, false)
        addView(view)
        textView = view.findViewById(R.id.text_view)
        iconImageView = view.findViewById(R.id.icon_image_view)
        val backgroundImageView =
            view.findViewById<ImageView>(R.id.background_image_view)
        selectedStyleLayout = view.findViewById(R.id.selected_style_layout)

        selectedColor = loadColor(R.color.white)
        unselectedColor = loadColor(R.color.black)

        if (attrs != null) {
            val typedArray =
                context.obtainStyledAttributes(attrs, R.styleable.CardItem, 0, 0)
            try {
                val text = typedArray.getString(R.styleable.CardItem_text)
                val backgroundDrawableID =
                    typedArray.getResourceId(R.styleable.CardItem_background_drawable, 0)
                val iconDrawableID =
                    typedArray.getResourceId(R.styleable.CardItem_icon_drawable, 0)
                val textSize =
                    typedArray.getDimensionPixelSize(R.styleable.CardItem_text_size, 0)
                textView?.setText(text)
                if (textSize > 0) textView?.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    textSize.toFloat()
                )
                if (backgroundDrawableID != 0) backgroundImageView.setImageResource(
                    backgroundDrawableID
                )
                if (iconDrawableID != 0) iconImageView?.setImageResource(iconDrawableID)
            } finally {
                typedArray.recycle()
            }
        }

        setIsSelected(false)

    }

    private fun loadColor(id: Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) resources.getColor(
            id,
            context.theme
        ) else resources.getColor(id)
    }
}