package maria.incyberspace.customviewattempt

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import com.google.android.material.slider.Slider

class CustomSlider @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : Slider(context, attrs, defStyleAttr) {

    init {
        this.apply {
            valueTo = 100f
            value = 50f
        }
    }

    override fun onDraw(c: Canvas) {
        c.rotate(270f)
        c.translate(-height.toFloat(), 0f)
        super.onDraw(c)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec)
        setMeasuredDimension(measuredHeight, measuredWidth);
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(h, w, oldh, oldw)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {

        if (!isEnabled) {
            return false
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
                onSizeChanged(width, height, 0, 0)
                super.onTouchEvent(
                    MotionEvent.obtain(event.downTime, event.eventTime, event.action,
                        height.toFloat() - event.y, event.x, event.pressure, event.size, event.metaState,event.yPrecision,event.xPrecision, event.deviceId, event.edgeFlags))
            }
        }
        return true
    }

}