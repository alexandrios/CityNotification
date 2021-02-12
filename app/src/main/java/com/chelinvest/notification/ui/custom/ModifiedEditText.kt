package com.chelinvest.notification.ui.custom

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import com.chelinvest.notification.R
import com.chelinvest.notification.additional.pxFromDp

open class ModifiedEditText : FrameLayout {

    enum class ImeOption {
        DONE,
        NEXT
    }

    constructor(context: Context) : super(context) { init(null) }
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) { init(attrs) }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context!!, attrs, defStyleAttr) { init(attrs) }

    var onTextChanged: ((text: String?) -> Unit)? = null

    private fun init(attrs: AttributeSet?) {
        val view = LayoutInflater.from(context).inflate(R.layout.view_modified_edit_text, this, false)
        addView(view)

        val editText = view.findViewById<EditText>(R.id.vEditText)

        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomEditText, 0, 0)
            try {
                val isNecessary = typedArray.getBoolean(R.styleable.CustomEditText_cet_isNecessary, false)
                val paddingStart = typedArray.getDimension(R.styleable.CustomEditText_cet_paddingStart, -1f).toInt()
                val hint = typedArray.getString(R.styleable.CustomEditText_cet_hint)
                val text = typedArray.getString(R.styleable.CustomEditText_cet_text)
                val textSize = if (typedArray.hasValue(R.styleable.CustomEditText_cet_textSize))
                    typedArray.getDimensionPixelSize(R.styleable.CustomEditText_cet_textSize, context.pxFromDp(12f).toInt()).toFloat()
                else
                    null
                val textColor = if (typedArray.hasValue(R.styleable.CustomEditText_cet_textColor))
                    typedArray.getColor(R.styleable.CustomEditText_cet_textColor, Color.BLACK)
                else
                    null
                val hintTextColor = if (typedArray.hasValue(R.styleable.CustomEditText_cet_hintTextColor))
                    typedArray.getColor(R.styleable.CustomEditText_cet_hintTextColor, Color.BLACK)
                else
                    null
                val imeOption = ImeOption.values()[typedArray.getInt(R.styleable.CustomEditText_cet_imeOption, 0) % ImeOption.values().size]
                val inputType = typedArray.getInt(R.styleable.CustomEditText_cet_inputType, 0) % 7
                val label = typedArray.getString(R.styleable.CustomEditText_cet_label)
                description = typedArray.getString(R.styleable.CustomEditText_cet_description)

                if (paddingStart >= 0)
                    editText.setPadding(paddingStart, editText.paddingTop, editText.paddingRight, editText.paddingBottom)
                editText.hint = hint
                editText.setText(text)
                if (textSize != null)
                    editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
                if (textColor != null)
                    editText.setTextColor(textColor)
                if (hintTextColor != null)
                    editText.setHintTextColor(hintTextColor)
                setImeOptions(imeOption)
                editText.inputType = when (inputType) {
                    1 -> InputType.TYPE_CLASS_NUMBER
                    2 -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                    3 -> InputType.TYPE_CLASS_PHONE
                    4 -> InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                    5 -> InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
                    6 -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    else -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
                }

                if (!label.isNullOrEmpty() || isNecessary) {
                    var labelText = label ?: ""
                    if (labelText.isEmpty() && isNecessary)
                        labelText = "Обязательное поле"
                    if (isNecessary)
                        labelText += "*"
                    val labelTextView = view.findViewById<TextView>(R.id.vLabelTextView)
                    labelTextView.visibility = View.VISIBLE
                    labelTextView.text = labelText
                }
            } catch (_: Exception) {
            } finally {
                typedArray.recycle()
            }
        }

        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && error != null)
                error = null
        }
        editText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                onTextChanged?.let { onTextChanged ->
                    onTextChanged(s?.toString())
                }
            }
        })
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        findViewById<View>(R.id.vEditText).isEnabled = enabled
    }

    fun getEditText(): EditText {
        return findViewById(R.id.vEditText)
    }

    fun getText(): String {
        return findViewById<EditText>(R.id.vEditText)?.text?.toString() ?: ""
    }

    fun setText(text: String) {
        findViewById<EditText>(R.id.vEditText)?.setText(text)
    }

    fun setHint(text: String) {
        findViewById<EditText>(R.id.vEditText)?.hint = text
    }

    var description: String? = null
        set(value) {
            field = value
            val descriptionTextView = findViewById<TextView>(R.id.vDescriptionTextView)
            descriptionTextView.visibility = if (value == null) View.GONE else View.VISIBLE
            descriptionTextView.text = value ?: ""
        }

    var error: String? = null
        set(value) {
            field = value
            val editText = findViewById<EditText>(R.id.vEditText)
            val errorTextView = findViewById<TextView>(R.id.vErrorTextView)
            editText.setBackgroundResource(if (value == null) R.drawable.bg_modified_edit_text else R.drawable.bg_modified_error_edit_text)
            errorTextView.visibility = if (value == null) View.GONE else View.VISIBLE
            errorTextView.text = value ?: ""
        }

    fun setImeOptions(imeOption: ImeOption) {
        findViewById<EditText>(R.id.vEditText)?.imeOptions = when (imeOption) {
            ImeOption.NEXT -> EditorInfo.IME_ACTION_NEXT
            else -> EditorInfo.IME_ACTION_DONE
        }
    }

}