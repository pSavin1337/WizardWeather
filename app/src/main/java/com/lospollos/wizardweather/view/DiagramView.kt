package com.lospollos.wizardweather.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import com.lospollos.wizardweather.R
import kotlin.math.roundToInt

class DiagramView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textRect = Rect()
    private var humidityText = ""

    var humidity: Int = 0
        set(value) {
            field = value
            humidityText = "Влажность: $field%"
            invalidate()
        }

    private var circleColor: Int = Color.BLUE
        set(value) {
            field = value
            backgroundPaint.color = value
            invalidate()
        }

    init {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.DiagramView, 0, 0)

        val counterTextSize = typedArray.getDimensionPixelSize(
            R.styleable.DiagramView_textSize,
            (64f * resources.displayMetrics.scaledDensity).roundToInt()
        ).toFloat()

        val textColor = typedArray.getColor(R.styleable.DiagramView_textColor, Color.WHITE)
        circleColor = typedArray.getColor(R.styleable.DiagramView_circleColor, Color.BLUE)

        textPaint.apply {
            color = textColor
            textSize = counterTextSize
        }

        typedArray.recycle()
    }

    override fun onDraw(canvas: Canvas?) {

        backgroundPaint.color = circleColor

        textPaint.getTextBounds(humidityText, 0, humidityText.length, textRect)

        canvas?.drawArc(
            60f,
            40f,
            width - 60f,
            height - 80f,
            0f,
            (humidity * 3.6).toFloat(),
            true, backgroundPaint
        )

        canvas?.drawText(
            humidityText,
            20f,
            height - 20f,
            textPaint
        )

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val maxTextWidth = textPaint.measureText("Влажность: 100%")

        val desiredWidth = (maxTextWidth + paddingLeft.toFloat() + paddingRight.toFloat())
            .roundToInt()
        val desiredHeight = (maxTextWidth + paddingTop.toFloat() + paddingBottom.toFloat())
            .roundToInt()

        val measuredWidth = resolveSize(desiredWidth, widthMeasureSpec)
        val measuredHeight = resolveSize(desiredHeight, heightMeasureSpec)

        setMeasuredDimension(measuredWidth, measuredHeight)
    }

}