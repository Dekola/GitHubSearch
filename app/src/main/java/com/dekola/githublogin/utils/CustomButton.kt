package com.dekola.githublogin.utils

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.dekola.githublogin.R
import com.dekola.githublogin.databinding.LayoutCustomButtonBinding

class CustomButton : ConstraintLayout {

    private var buttonText: String? = null
    private val binding: LayoutCustomButtonBinding =
        LayoutCustomButtonBinding.inflate(LayoutInflater.from(context), this, true)

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setCustomAttribute(attrs)
    }

    private fun setCustomAttribute(attrs: AttributeSet?) {
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomButton)
        buttonText = typedArray.getString(R.styleable.CustomButton_buttonText)
        val textColor = typedArray.getColor(R.styleable.CustomButton_textColor, Color.WHITE)
        binding.button.text = buttonText
        binding.button.setTextColor(textColor)
        typedArray.recycle()
    }

    override fun setOnClickListener(listener: OnClickListener?) {
        binding.button.setOnClickListener(listener)
    }

    fun load(load: Boolean) {
        binding.button.text = if (load) "" else buttonText
        binding.button.isClickable = !load
        this.isClickable = !load

        val color = if (load) R.color.colorFadedPrimary else R.color.colorPrimary
        binding.button.setBackgroundColor(ContextCompat.getColor(context, color))

        binding.progress.visibility = if (load) View.VISIBLE else View.GONE
    }

}
