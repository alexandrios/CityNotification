package com.chelinvest.notification.ui.custom.stylable

import android.content.Context
import android.graphics.Typeface

enum class TypefaceStyle { REGULAR, BOLD, LIGHT }

fun getTypefaceByStyle(context: Context, style: Int): Typeface {
    val values = TypefaceStyle.values()
    val typefaceStyle = if (style >= 0 && style < values.size) values[style] else TypefaceStyle.REGULAR
    return getTypefaceByStyle(context, typefaceStyle)
}

fun getTypefaceByStyle(context: Context, style: TypefaceStyle): Typeface {
    return when (style) {
        TypefaceStyle.REGULAR -> Typefaces.getDefaultRegular(context)
        TypefaceStyle.BOLD -> Typefaces.getDefaultBold(context)
        TypefaceStyle.LIGHT -> Typefaces.getDefaultLight(context)
    }
}

fun getBebasNeueRegular(context: Context) = Typefaces.getBebasNeueRegular(context)