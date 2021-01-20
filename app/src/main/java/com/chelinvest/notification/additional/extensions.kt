package com.chelinvest.notification.additional

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.chelinvest.notification.R
import com.chelinvest.notification.utils.Constants.GROUP_LOGOS_DIRECTORY_NAME
import com.chelinvest.notification.utils.Constants.SERVICE_LOGOS_DIRECTORY_NAME
import java.io.File
import java.util.*

// This method was deprecated in API level 28 !!!  Use InputMethodService.requestHideSelf(int) instead.
fun hideSoftKeyboard(activity: Activity?) {
    if (activity == null) return
    if (activity.currentFocus == null) return

    val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
}

fun showSoftKeyboard(activity: Activity?) {
    if (activity == null) return
    if (activity.currentFocus == null) return

    val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
    //inputMethodManager.showSoftInputFromInputMethod(activity.getCurrentFocus().getWindowToken(), 0);
}

fun Context.color(colorId: Int): Int = ContextCompat.getColor(this, colorId)

fun Context.drawable(drawableId: Int): Drawable? = ContextCompat.getDrawable(this, drawableId)

fun Context.string(stringId: Int): String = this.getString(stringId)

fun Context.string(stringId: Int, vararg args: Any): String = this.getString(stringId).format(Locale.ENGLISH, *args)

/**
 * Переводит dp в пиксели
 * @param dp размер в dp
 * @return размер в пикселях
 */
fun Context.pxFromDp(dp: Float): Float = dp * resources.displayMetrics.density

fun String.short(): String {
    //val words = split("[,;:.!?()\"'+-=/*\\s]+".toRegex())
    val str = this.trim()
    val words = str.split("[\\P{L}]+".toRegex())
    var res = ""
    run loop@ {
        words.forEach { word ->
            if (word.isNotEmpty())
                res += word[0]
            if (res.length == 2)
                return@loop
        }
    }
    if (res.length < 2) {
        run loop@ {
            words.forEach { word ->
                if (word.length > 1)
                    res += word[1]
                if (res.length == 2)
                    return@loop
            }
        }
    }
    if (res.isEmpty()) {
        res = str.substring(0, kotlin.math.min(2, str.length))
    }
    return res
}
/*
fun Context.serviceIcon(ind: Int): Drawable? {
    val iconArray = resources.obtainTypedArray(R.array.service_icons)
    val drawable = iconArray.getDrawable(ind)
    iconArray.recycle()
    return drawable
}

fun Context.shapeGradient(ind: Int, shape: Int): Drawable {
    val colorArray = resources.obtainTypedArray(R.array.gradients)
    val color1 = colorArray.getColor(ind * 2, color(R.color.tangelo))
    val color2 = colorArray.getColor(ind * 2 + 1, color(R.color.tangelo))
    colorArray.recycle()
    val gradient = GradientDrawable(GradientDrawable.Orientation.BL_TR, arrayOf(color1, color2).toIntArray())
    if (shape == 0) {
        gradient.shape = GradientDrawable.OVAL
    }
    if (shape == 1) {
        gradient.shape = GradientDrawable.RECTANGLE
        gradient.cornerRadius = pxFromDp(10f)
    }
    if (shape == 2) {
        gradient.shape = GradientDrawable.RECTANGLE
    }
    return gradient
}
*/

fun Context.gradientColor(ind: Int): Int {
    val colorArray = resources.obtainTypedArray(R.array.gradients)
    val color = colorArray.getColor(ind * 2, color(R.color.tangelo))
    colorArray.recycle()
    return color
}


fun Context.groupLogosDir(): File {
    val dir = File(filesDir, GROUP_LOGOS_DIRECTORY_NAME)
    if (!dir.exists())
        dir.mkdir()
    return dir
}

fun Context.serviceLogosDir(): File {
    val dir = File(filesDir, SERVICE_LOGOS_DIRECTORY_NAME)
    if (!dir.exists())
        dir.mkdir()
    return dir
}

fun Context.setKeyboardVisibility(view: View, visibility: Boolean) {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    if (visibility)
        inputMethodManager?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    else
        inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Drawable.setColor(@ColorInt color: Int) {
    this.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN)
}

fun ImageView.setColor(@ColorInt color: Int) {
    this.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN)
}

fun ImageView.setColorRes(@ColorRes colorId: Int) {
    this.setColorFilter(context.color(colorId), android.graphics.PorterDuff.Mode.SRC_IN)
}

fun String.nullIfEmpty(): String? {
    if (this.isEmpty())
        return null
    return this
}

fun factorial(x: Int): Long {
    return when (x) {
        1 -> 1
        else -> {
            x * factorial(x - 1)
        }
    }
}