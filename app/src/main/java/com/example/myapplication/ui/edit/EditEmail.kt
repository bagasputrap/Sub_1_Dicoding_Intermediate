package com.example.myapplication.ui.edit

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.example.myapplication.utility.Constanta

class EditEmail : AppCompatEditText {


    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                error = if (s.isNotEmpty()) {
                    if (!s.toString().matches(Constanta.emailPattern)) {
                        context.getString(R.string.validation_invalid_email)
                    } else null
                } else null
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        context.apply {
            background = ContextCompat.getDrawable(this, R.drawable.custom_form_input)
            setTextColor(ContextCompat.getColor(this, R.color.pink1))
            setHintTextColor(ContextCompat.getColor(this, R.color.pink1))
        }
        isSingleLine = true
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }
}
