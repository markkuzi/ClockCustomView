package com.example.clockcustomview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import java.util.*
import kotlin.math.*
import kotlin.properties.Delegates

class ClockCustomVIew(
    context: Context,
    attributeSet: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int
) : View(context, attributeSet, defStyleAttr, defStyleRes) {

    private var hourHandColor by Delegates.notNull<Int>()
    private var hourHandWidth by Delegates.notNull<Float>()
    private var minuteHandColor by Delegates.notNull<Int>()
    private var minuteHandWidth by Delegates.notNull<Float>()
    private var secondHandColor by Delegates.notNull<Int>()
    private var secondHandWidth by Delegates.notNull<Float>()
    private var clockDivisionColor by Delegates.notNull<Int>()
    private var clockBackgroundColor by Delegates.notNull<Int>()
    private var strokeClockColor by Delegates.notNull<Int>()
    private var strokeClockWidth by Delegates.notNull<Float>()
    private var numberColor by Delegates.notNull<Int>()
    private var numberSize by Delegates.notNull<Float>()
    private var numberSizeActual by Delegates.notNull<Float>()

    private lateinit var hourHandPaint: Paint
    private lateinit var minuteHandPaint: Paint
    private lateinit var secondHandPaint: Paint
    private lateinit var clockDivisionPaint: Paint
    private lateinit var clockStrokePaint: Paint
    private lateinit var numberPaint: Paint
    private lateinit var clockBackgroundPaint: Paint

    private lateinit var date: Calendar
    private var timeZon = DEFAULT_TIME_ZONE
    private var hour by Delegates.notNull<Float>()
    private var minute by Delegates.notNull<Float>()
    private var second by Delegates.notNull<Float>()
    private var corner by Delegates.notNull<Double>()

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : this(context, attributeSet, defStyleAttr, R.style.DefaultClockCustomStyle)
    constructor(context: Context, attributeSet: AttributeSet?) :this(context, attributeSet, R.attr.clockCustomViewStyle)
    constructor(context: Context) : this(context, null)


    init {
        if (attributeSet != null) {
            initAttributes(attributeSet, defStyleAttr, defStyleRes)
        }
        else{
            initDefaultsAttributes()
        }
        initPaints()
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        drawClock(canvas)
        drawClockDivision(canvas)
        drawHands(canvas)
        drawNumber(canvas)
        invalidate()
    }

    private fun drawClock(canvas: Canvas?) {
        canvas?.drawCircle(width/2f, height/2f, min(width, height) / 2f-40f, clockBackgroundPaint)
        canvas?.drawCircle(width/2f, height/2f, min(width, height) / 2f-40f, clockStrokePaint)
    }

    private fun drawClockDivision(canvas: Canvas?) {
        for (i in 1..60) {
            clockDivisionPaint.strokeWidth = minuteHandWidth
            val radius = getRadius()-50f
            var startRatio = 5.6f
            if (i % 5 == 0){
                clockDivisionPaint.strokeWidth = minuteHandWidth
                startRatio = 5.3f
            }
            val (x1,y1) = getCoordinates(i * 1f, startRatio / 6f * radius)
            val (x2,y2) = getCoordinates(i * 1f, 5.9f / 6f * radius)
            canvas?.drawLine(x1,y1,x2,y2, clockDivisionPaint)
        }
    }

    private fun drawHands(canvas: Canvas?) {
        //timeZon = clockCustomTimeZone?.timeZone.toString() ?: "GMT"
        date = Calendar.getInstance(TimeZone.getTimeZone(timeZon))
        hour = date[Calendar.HOUR] + date[Calendar.MINUTE] / 60f
        minute = date[Calendar.MINUTE] + date[Calendar.SECOND] / 60f
        second = date[Calendar.SECOND].toFloat()
        drawHand(canvas,hour ,hourHandPaint, getRadius()/3)
        drawHand(canvas, minute, minuteHandPaint, getRadius()/2)
        drawHand(canvas, second, secondHandPaint,  getRadius()-50f)
        canvas?.drawCircle(width/2f, height/2f, 20f, secondHandPaint)
    }

    private fun drawHand(canvas: Canvas?, date: Float, paint: Paint, handLength: Float) {
        corner = if (paint == hourHandPaint){
            Math.PI * date / 6 - Math.PI / 2
        } else{
            Math.PI * date / 30 - Math.PI / 2
        }
        canvas?.drawLine(
            width / 2f,
            height / 2f,
            (width / 2f + cos(corner) * handLength).toFloat(),
            (height / 2f + sin(corner) * handLength).toFloat(),
            paint
        )
    }

    private fun drawNumber(canvas: Canvas?) {
        numberSizeActual = if (numberSize == 0f) {
            getRadius()/8f
        } else
            numberSize
        for (i in 1..12) {
            if (i in arrayListOf(3, 6, 9, 12)) {
                numberPaint.textSize = numberSizeActual * 1.5f
            }
            else {
                numberPaint.textSize = numberSizeActual
            }
            val radius = getRadius()-60f
            val (x, y) = getCoordinates(i * 5f, 4.8f / 6f * radius)
            val bounds = Rect()
            numberPaint.getTextBounds(i.toString(), 0, i.toString().length, bounds)
            canvas?.drawText(
                i.toString(),
                x - bounds.width() / 2f,
                y + bounds.height() / 2f,
                numberPaint
            )
        }
    }

    private fun getCoordinates(seconds: Float, r: Float, c: Pair<Float,Float> = getCenter()): Pair<Float, Float> {
        val angle = max(seconds, 0.001f) * 2 * Math.PI / 60f
        val y = r * (1 - cos(angle))
        val x = y / tan(angle / 2)
        return Pair(c.first + x.toFloat(), c.second - r + y.toFloat())
    }

    private fun getRadius() = min((width-paddingLeft-paddingRight), (height-paddingTop-paddingBottom)) / 2f

    private fun getCenter() = Pair(width / 2f, height / 2f)

    private fun initPaints() {
        hourHandPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        hourHandPaint.color = hourHandColor
        hourHandPaint.style = Paint.Style.FILL
        hourHandPaint.strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, hourHandWidth, resources.displayMetrics)
        hourHandPaint.setShadowLayer(5f, 5f, 0f, SHADOW_COLOR)
        hourHandPaint.strokeCap = Paint.Cap.ROUND

        minuteHandPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        minuteHandPaint.color = minuteHandColor
        minuteHandPaint.style = Paint.Style.FILL
        minuteHandPaint.strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, minuteHandWidth, resources.displayMetrics)
        minuteHandPaint.setShadowLayer(5f, 5f, 0f, SHADOW_COLOR)
        minuteHandPaint.strokeCap = Paint.Cap.ROUND

        secondHandPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        secondHandPaint.color = secondHandColor
        secondHandPaint.style = Paint.Style.FILL
        secondHandPaint.strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, secondHandWidth, resources.displayMetrics)
        secondHandPaint.setShadowLayer(5f, 5f, 0f, SHADOW_COLOR)
        secondHandPaint.strokeCap = Paint.Cap.ROUND

        clockDivisionPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        clockDivisionPaint.color = strokeClockColor
        clockDivisionPaint.style = Paint.Style.FILL

        clockStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        clockStrokePaint.color = strokeClockColor
        clockStrokePaint.style = Paint.Style.STROKE
        clockStrokePaint.strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, strokeClockWidth, resources.displayMetrics)
        clockStrokePaint.setShadowLayer(20f, 10f, 0f, SHADOW_COLOR)

        numberPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        numberPaint.color = numberColor
        numberPaint.textSize = numberSize

        clockBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        clockBackgroundPaint.color = clockBackgroundColor
        clockBackgroundPaint.style = Paint.Style.FILL
    }

    private fun initAttributes(attributeSet: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ClockCustomVIew, defStyleAttr, defStyleRes)
        hourHandColor = typedArray.getColor(R.styleable.ClockCustomVIew_hourHandColor, HOUR_HAND_DEFAULT_COLOR)
        hourHandWidth = typedArray.getDimension(R.styleable.ClockCustomVIew_hourHandWidth, HOUR_HAND_DEFAULT_WIDTH)
        minuteHandColor = typedArray.getColor(R.styleable.ClockCustomVIew_minuteHandColor, MINUTE_HAND_DEFAULT_COLOR)
        minuteHandWidth = typedArray.getDimension(R.styleable.ClockCustomVIew_minuteHandWidth, MINUTE_HAND_DEFAULT_WIDTH)
        secondHandColor = typedArray.getColor(R.styleable.ClockCustomVIew_secondHandColor, SECOND_HAND_DEFAULT_COLOR)
        secondHandWidth = typedArray.getDimension(R.styleable.ClockCustomVIew_secondHandWidth, SECOND_HAND_DEFAULT_WIDTH)
        clockDivisionColor = typedArray.getColor(R.styleable.ClockCustomVIew_clockDivisionColor, CLOCK_DIVISION_DEFAULT_COLOR)
        strokeClockColor = typedArray.getColor(R.styleable.ClockCustomVIew_strokeClockColor, STROKE_CLOCK_DEFAULT_COLOR)
        strokeClockWidth = typedArray.getDimension(R.styleable.ClockCustomVIew_strokeClockWidth, STROKE_CLOCK_DEFAULT_WIDTH)
        clockBackgroundColor = typedArray.getColor(R.styleable.ClockCustomVIew_clockBackgroundColor, CLOCK_BACKGROUND_DEFAULT_COLOR)
        numberColor = typedArray.getColor(R.styleable.ClockCustomVIew_numberColor, NUMBER_DEFAULT_COLOR)
        numberSize = typedArray.getDimension(R.styleable.ClockCustomVIew_numberSize, NUMBER_DEFAULT_SIZE)
        typedArray.recycle()
    }

    private fun initDefaultsAttributes() {
        hourHandColor = HOUR_HAND_DEFAULT_COLOR
        hourHandWidth = HOUR_HAND_DEFAULT_WIDTH
        minuteHandColor = MINUTE_HAND_DEFAULT_COLOR
        minuteHandWidth = MINUTE_HAND_DEFAULT_WIDTH
        secondHandColor = SECOND_HAND_DEFAULT_COLOR
        secondHandWidth = SECOND_HAND_DEFAULT_WIDTH
        strokeClockColor = STROKE_CLOCK_DEFAULT_COLOR
        strokeClockWidth = STROKE_CLOCK_DEFAULT_WIDTH
        clockBackgroundColor = CLOCK_BACKGROUND_DEFAULT_COLOR
        numberColor = NUMBER_DEFAULT_COLOR
        numberSize = NUMBER_DEFAULT_SIZE
    }

    fun setTimeZone(zone: String){
        timeZon = zone
    }

    companion object {
        const val HOUR_HAND_DEFAULT_COLOR = Color.BLACK
        const val HOUR_HAND_DEFAULT_WIDTH = 6f
        const val MINUTE_HAND_DEFAULT_COLOR = Color.BLACK
        const val MINUTE_HAND_DEFAULT_WIDTH = 4f
        const val SECOND_HAND_DEFAULT_COLOR = Color.RED
        const val SECOND_HAND_DEFAULT_WIDTH = 2f
        const val CLOCK_DIVISION_DEFAULT_COLOR = Color.BLACK
        const val CLOCK_BACKGROUND_DEFAULT_COLOR = Color.TRANSPARENT
        const val STROKE_CLOCK_DEFAULT_COLOR = Color.BLACK
        const val STROKE_CLOCK_DEFAULT_WIDTH = 5f
        const val NUMBER_DEFAULT_COLOR = Color.BLACK
        const val NUMBER_DEFAULT_SIZE = 0f
        const val SHADOW_COLOR = Color.BLACK
        const val DEFAULT_TIME_ZONE = "GMT"
    }
}
